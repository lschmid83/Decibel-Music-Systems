using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Linq;
using System.Net;
using System.Web.Script.Serialization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using DecibelRestWpf.Http;

namespace DecibelRestWpf.UserControls
{
    /// <summary>
    /// Searches for participants using the Decibel REST API.
    /// </summary>
    public partial class ParticipantSearch : UserControl, ISearchUserControl
    {
        #region Fields

        /// <summary>
        /// The collection of participants returned from the search.
        /// </summary>
        private Participant[] mParticipants;

        /// <summary>
        /// The collection of participant associations returned from the search.
        /// </summary>
        private ParticipantAssociate[] mParticipantAssociations;

        /// <summary>
        /// The participant search parameters.
        /// </summary>
        private ParticipantSearchParam mSearchParameters;

        /// <summary>
        /// The participant search client. 
        /// </summary>
        private WebClient mSearchClient;

        /// <summary>
        /// The participant associations search client.
        /// </summary>
        private WebClient mAssociatesClient;

        /// <summary>
        /// The participant track appearances search client.
        /// </summary>
        private WebClient mAppearancesClient;

        /// <summary>
        /// The participant search query result ID.
        /// </summary>
        private Guid mQueryResultID;

        /// <summary>
        /// The participant information search depth.
        /// </summary>
        private string mParticipantDepth;

        /// <summary>
        /// Background worker thread which updates the response text box. 
        /// </summary>
        private BackgroundWorker mSetResponse;

        #endregion Fields

        #region EventHandlers

        /// <summary>
        /// Occurs when a search begins.
        /// </summary>
        public event BeginSearchEventHandler BeginSearch;

        /// <summary>
        /// Occurs when a search is complete.
        /// </summary>
        public event EndSearchEventHandler EndSearch;

        #endregion EventHandlers

        #region Constructors

        /// <summary>
        /// Creates new ParticipantSearch UserControl.
        /// </summary>
        public ParticipantSearch()
        {
            InitializeComponent();
            InitUserControl();
        }

        #endregion Constructors

        #region Methods

        /// <summary>
        /// Initializes the UserControl.
        /// </summary>
        public void InitUserControl()
        {
            // Initialize the search parameters
            mSearchParameters = new ParticipantSearchParam { Depth = "Names;" };

            // Clear the search fields
            txtSearchDateBorn.Text = String.Empty;
            txtSearchDateDied.Text = String.Empty;

            // Clear the search results
            dtgParticipantResults.ItemsSource = null;
            dtgTrackAppearances.ItemsSource = null;
            lstParticipantAssociates.Items.Clear();
            txtResponse.Text = String.Empty;
            pageNavigator.Visibility = Visibility.Hidden;

            // Initialize the autocomplete fields
            InitAutoComplete();

            // Initialize the search depth text field
            mParticipantDepth = "Relationships;Names;";
            txtSearchDepth.Text = Util.GetSearchDepthTruncated(mParticipantDepth);

            // Initialize the search URL text field 
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);

            // Register the page navigator event handler
            pageNavigator.PageChanged += PageChanged;

        }

        /// <summary>
        /// Initializes the AutoCompleteBox controls.
        /// </summary>
        public void InitAutoComplete()
        {
            Util.InitAutocompleteProperties(txtSearchName);
            Util.InitAutocompleteProperties(txtSearchActivity);
        }

        /// <summary>
        /// Gets the participant search URL.
        /// </summary>
        /// <param name="searchParam">The participant search parameters.</param>
        /// <returns>The participant search URL.</returns>
        private string GetParticipantSearchUrl(ParticipantSearchParam searchParam)
        {
            DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
            searchParam.Format = Settings.ResponseFormat.ToString().ToLower();
            Uri uri = urlBuilder.GetParticipantSearchUrl(searchParam);
            return uri.AbsoluteUri;
        }

        /// <summary>
        /// Occurs when the search button is clicked. 
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnSearch_Click(object sender, RoutedEventArgs e)
        {
            Search(new Uri(txtSearchUrl.Text));
            pageNavigator.Visibility = Visibility.Hidden;
            pageNavigator.NumberOfPages = 1;
            pageNavigator.SetCurrentPage(1, false);
        }

        /// <summary>
        /// Begins the search.
        /// </summary>
        /// <param name="url">The REST request URL.</param>
        public void Search(Uri url)
        {
            // Cancel any pending searches
            if (mSearchClient != null)
                mSearchClient.CancelAsync();

            if (mAssociatesClient != null)
                mAssociatesClient.CancelAsync();

            if (mAppearancesClient != null)
                mAppearancesClient.CancelAsync();

            // Clear the search results
            dtgParticipantResults.ItemsSource = null;
            dtgTrackAppearances.ItemsSource = null;
            lstParticipantAssociates.Items.Clear();
            tabTrackInformation.Header = "Track Appearances";
            mParticipants = null;

            // Issue a request to search for participants
            mSearchClient = new WebClient();
            Util.SetDecibelRequestHeaders(mSearchClient);
            mSearchClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(ParticipantSearch_Completed);
            mSearchClient.DownloadStringAsync(url);
            this.BeginSearch(this, new BeginSearchEventArgs() { Uri = url });
        }

        /// <summary>
        /// Occurs when a page is selected from the result set.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        public void PageChanged(object sender, PageNavigator.PageChangedEventArgs e)
        {
            if (mQueryResultID != Guid.Empty)
            {
                // Request a results page
                String url = Settings.ApiAddress + "Participants/Pages/" + mQueryResultID + "?pageNumber=" + (e.PageNumber - 1) + "&format=" + Settings.ResponseFormat; 
                Search(new Uri(url));
            }
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void ParticipantSearch_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            if(e.Error == null)
            {
                // Deserialize the response 
                ParticipantQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<ParticipantQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<ParticipantQueryResult>(e.Result);   
                mParticipants = queryResult.ResultSet;

                // Store the query result ID for paged results
                if (pageNavigator.CurrentPage == 1)
                    mQueryResultID = queryResult.QueryResultID;
                
                // Initialize the page navigator
                if (queryResult.Pages > 1)
                {
                    pageNavigator.NumberOfPages = queryResult.Pages;
                    pageNavigator.Visibility = Visibility.Visible;
                }

                // Initialize the results table
                DataTable dataTable = new DataTable();
                dataTable.Columns.Add("Index", typeof(int));
                dataTable.Columns.Add("Name", typeof(string));
                dataTable.Columns.Add("Gender", typeof(string));
                dataTable.Columns.Add("DateBorn", typeof(string));
                dataTable.Columns.Add("DateDied", typeof(string));
                int count = 0;
                foreach (Participant participant in mParticipants)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = count + 1;
                    row["Name"] = participant.Name;
                    row["Gender"] = participant.Gender;
                    row["DateBorn"] = participant.DateBorn.Name;
                    row["DateDied"] = participant.DateDied.Name;
                    dataTable.Rows.Add(row);
                    count++;
                }
                dtgParticipantResults.IsReadOnly = true;
                dtgParticipantResults.ItemsSource = dataTable.AsDataView();

                // Update the response text box
                mSetResponse = new BackgroundWorker();
                mSetResponse.DoWork += new DoWorkEventHandler(mSetResponse_DoWork);
                mSetResponse.RunWorkerAsync(e.Result);

                if (dtgParticipantResults.Items.Count > 0)
                    Util.SetDatagridItem(dtgParticipantResults, 0, 0);
                else
                    this.EndSearch(this, new EndSearchEventArgs() { ResultCount = 0 });
            }
            else
            {
                if (!e.Cancelled)
                    this.EndSearch(this, new EndSearchEventArgs() { ResultCount = 0 });
                Util.CheckAuthentication(e.Error);
                txtResponse.Text = String.Empty; 
            }
        }

        /// <summary>
        /// Occurs when a participant is selected from the results table.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void dtgParticipantResults_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // Refresh the participant associates information
            DataRowView drv = (DataRowView)dtgParticipantResults.SelectedItem;
            if (drv != null)
            {
                int sel = Int32.Parse(drv[0].ToString()) - 1;
                if (mParticipants != null && sel > -1 && sel < mParticipants.Length)
                {
                    RefreshParticipantAssociates(mParticipants[sel]);
                    tabTrackInformation.Header = "Track Appearances [" + mParticipants[sel].Name + "]";
                    RefreshParticipantTrackAppearances(mParticipants[sel]);
                }
            }
        }

        /// <summary>
        /// Refreshes the participant associates information.
        /// </summary>
        /// <param name="participant">The Participant object.</param>
        private void RefreshParticipantAssociates(Participant participant)
        {
            // Cancel any pending requests
            if (mAssociatesClient != null)
                mAssociatesClient.CancelAsync();
            
            lstParticipantAssociates.Items.Clear();

            if (mParticipantDepth.Split(';').ToList().Contains("Relationships"))
            {
                // Issue a request for participant associates
                Uri uri = new Uri(Settings.ApiAddress + "Participants/" + participant.ID + "/Associates" + "?format=" + Settings.ResponseFormat);
                mAssociatesClient = new WebClient();
                Util.SetDecibelRequestHeaders(mAssociatesClient);
                mAssociatesClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(ParticipantAssociates_Completed);
                mAssociatesClient.DownloadStringAsync(uri);
                this.BeginSearch(this, new BeginSearchEventArgs { Uri = uri });
            }
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void ParticipantAssociates_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            if(e.Error == null)
            {
                // Deserialize the response 
                ParticipantAssociateQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<ParticipantAssociateQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<ParticipantAssociateQueryResult>(e.Result);

                // Initialize the list of participant associates
                mParticipantAssociations = queryResult.ResultSet;
                foreach (ParticipantAssociate participantAssociate in mParticipantAssociations)
                {
                    Participant participant = participantAssociate.Participant;
                    if (!String.IsNullOrWhiteSpace(participant.Name))
                        lstParticipantAssociates.Items.Add(participant.Name);
                }
            }
            else
            {
                lstParticipantAssociates.Items.Clear();
            }

            if (!e.Cancelled && !mAppearancesClient.IsBusy)
                this.EndSearch(this, new EndSearchEventArgs() { ResultCount = dtgParticipantResults.Items.Count });
        }

        /// <summary>
        /// Occurs when a participant associate is selected from the list.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void lstParticipantAssociates_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            int sel = lstParticipantAssociates.SelectedIndex;
            if (mParticipantAssociations != null && sel > -1 && sel < mParticipantAssociations.Length)
            {
                Participant participant = mParticipantAssociations[lstParticipantAssociates.SelectedIndex].Participant;
                tabTrackInformation.Header = "Track Appearances [" + participant.Name + "]";
                RefreshParticipantTrackAppearances(participant);
            }
        }

        /// <summary>
        /// Refreshes the participant track appearances.
        /// </summary>
        /// <param name="participant">The Participant object.</param>
        private void RefreshParticipantTrackAppearances(Participant participant)
        {
            // Cancel any pending requests
            if (mAppearancesClient != null)
                mAppearancesClient.CancelAsync();

            dtgTrackAppearances.ItemsSource = null;

            // Issue a request for participant track appearances
            Uri uri = new Uri(Settings.ApiAddress + "Participants/" + participant.ID + "/Tracks" + "?format=" + Settings.ResponseFormat);
            mAppearancesClient = new WebClient();
            Util.SetDecibelRequestHeaders(mAppearancesClient);
            mAppearancesClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(ParticipantTrackAppearances_Completed);
            mAppearancesClient.DownloadStringAsync(uri);
            this.BeginSearch(this, new BeginSearchEventArgs { Uri = uri });
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void ParticipantTrackAppearances_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            if(e.Error == null)
            {
                // Deserialize the response 
                ParticipantTrackAppearanceQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<ParticipantTrackAppearanceQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<ParticipantTrackAppearanceQueryResult>(e.Result);

                // Initialize the results table
                DataTable dataTable = new DataTable();
                dataTable.Columns.Add("Index", typeof(int));
                dataTable.Columns.Add("TrackName", typeof(string));
                dataTable.Columns.Add("AlbumName", typeof(string));
                dataTable.Columns.Add("ActivityName", typeof(string));
                int index = 1;
                foreach (TrackAppearance trackAppearance in queryResult.ResultSet)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = index;
                    row["TrackName"] = trackAppearance.TrackName;
                    row["AlbumName"] = trackAppearance.AlbumName;
                    row["ActivityName"] = trackAppearance.ActivityName;
                    dataTable.Rows.Add(row);
                    index++;
                }

                dtgTrackAppearances.IsReadOnly = true;
                dtgTrackAppearances.ItemsSource = dataTable.AsDataView();
            }

            if (!e.Cancelled && !mAssociatesClient.IsBusy)
                this.EndSearch(this, new EndSearchEventArgs() { ResultCount = dtgParticipantResults.Items.Count });
        }

        /// <summary>
        /// Occurs when the depth button is clicked. 
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnSelectDepth_Click(object sender, RoutedEventArgs e)
        {
            SearchDepth winSearchDepth = new SearchDepth();
            winSearchDepth.ShowDialog(SearchDepth.GetParticipantDepthParameters(), mParticipantDepth.Split(';'));
            if(winSearchDepth.DialogResult == true)   
            {
                mParticipantDepth = winSearchDepth.GetCheckedItems();
                txtSearchDepth.Text = Util.GetSearchDepthTruncated(mParticipantDepth);
            }
        }

        /// <summary>
        /// Refreshes the search URL.
        /// </summary>
        public void RefreshSearchUrl()
        {
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Sets the response format.
        /// </summary>
        /// <param name="responseFormat">The response format.</param>
        public void SetResponseFormat(DecibelResponseFormat responseFormat)
        {
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);
            if (dtgParticipantResults.Items.Count > 0)
                Search(new Uri(txtSearchUrl.Text));
            tabResponse.Header = responseFormat.ToString();
        }

        /// <summary>
        /// Gets the response string.
        /// </summary>
        /// <returns>The XML or JSON response string.</returns>
        public String GetResponse()
        {
            return txtResponse.Text;
        }

        /// <summary>
        /// Background worker thread which updates the response TextBox.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mSetResponse_DoWork(object sender, DoWorkEventArgs e)
        {
            SetResponse(e.Argument.ToString());
        }

        /// <summary>
        /// Makes a thread-safe call to update the Text property of the response TextBox.
        /// </summary>
        /// <param name="response">The XML or JSON response string.</param>
        public void SetResponse(string response)
        {
            if (Settings.ResponseFormat == DecibelResponseFormat.JSON)
                response = Util.FormatJson(response);
            else
                response = Util.FormatXml(response);
            txtResponse.Dispatcher.Invoke((Action) delegate { txtResponse.Text = response; });
        }

        /// <summary>
        /// Sends a request for autocomplete suggestions.
        /// </summary>
        /// <param name="text">The search text.</param>
        /// <param name="dictionary">The autocomplete dictionary.</param>
        public void SendAutoCompleteRequest(string text, string dictionary)
        {
            WebClient client = new WebClient();
            Util.SetDecibelRequestHeaders(client);
            client.DownloadStringCompleted += new DownloadStringCompletedEventHandler(AutoComplete_Completed);
            client.DownloadStringAsync(new Uri(Settings.ApiAddress + "AutoComplete?text=" + text + "&dictionary=" + dictionary + "&limit=5" + "&format=" + Settings.ResponseFormat), dictionary);     
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void AutoComplete_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            if(e.Error == null)
            {
                // Deserialize the response 
                SuggestionQueryResult suggestions;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    suggestions = Util.DeserializeXmlString<SuggestionQueryResult>(e.Result);
                else
                    suggestions = new JavaScriptSerializer().Deserialize<SuggestionQueryResult>(e.Result);

                // Select the autocomplete dictionary
                List<string> autocomplete = new List<string>();
                if (e.UserState.Equals("Participants") && suggestions.Participants != null)
                {
                    foreach (Suggestion suggestion in suggestions.Participants)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchName.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("Activities") && suggestions.Activities != null)
                {
                    foreach (Suggestion suggestion in suggestions.Activities)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchActivity.ItemsSource = autocomplete;
                }
            }
        }

        /// <summary>
        /// Releases all resources used by the Component.
        /// </summary>
        public void Dispose()
        {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        /// <summary>
        /// Releases the unmanaged resources used by the Component and optionally releases the managed resources.
        /// </summary>
        /// <param name="disposing">True to release both managed and unmanaged resources; false to release only unmanaged resources.</param>
        protected virtual void Dispose(bool disposing)
        {
            if (!disposing) return;

            if (mSearchClient != null)
                mSearchClient.Dispose();

            if (mAssociatesClient != null)
                mAssociatesClient.Dispose();

            if (mAppearancesClient != null)
                mAppearancesClient.Dispose();

            if (mSetResponse != null)
                mSetResponse.Dispose();
        }
             
        #endregion Methods

        #region Properties

        /// <summary>
        /// Gets whether a search is in progress.
        /// </summary>
        /// <returns>True if a search is in progress; otherwise, false.</returns>
        public bool IsBusy
        {
            get
            {
                return (mSearchClient != null && mSearchClient.IsBusy) ||
                       (mAssociatesClient != null && mAssociatesClient.IsBusy) ||
                       (mAppearancesClient != null && mAppearancesClient.IsBusy);
            }
        }

        #endregion Properties

        #region KeyEvents

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchName_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Name = txtSearchName.Text;
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchName_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchName.Text, "Participants");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchName_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchName.IsDropDownOpen)
                btnSearch_Click(this, null);
        }

        /// <summary>
        /// Occurs when the SelectedDate property is changed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchDateBorn_SelectedDateChanged(object sender, SelectionChangedEventArgs e)
        {
            if (txtSearchDateBorn.SelectedDate != null)
                mSearchParameters.DateBorn = txtSearchDateBorn.SelectedDate.Value.ToString("dd-MMM-yyyy");
            else
                mSearchParameters.DateBorn = null;
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when the SelectedDate property is changed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchDateDied_SelectedDateChanged(object sender, SelectionChangedEventArgs e)
        {
            if (txtSearchDateDied.SelectedDate != null)
                mSearchParameters.DateDied = txtSearchDateDied.SelectedDate.Value.ToString("dd-MMM-yyyy");
            else
                mSearchParameters.DateDied = null;
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchActivity_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Activity = txtSearchActivity.Text;
            txtSearchUrl.Text = GetParticipantSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchActivity_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchActivity.Text, "Activities");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchActivity_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchActivity.IsDropDownOpen)
                btnSearch_Click(this, null);
        }

        #endregion KeyEvents
    }
}
