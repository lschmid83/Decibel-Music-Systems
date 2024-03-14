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
    /// Searches for works using the Decibel REST API.
    /// </summary>
    public partial class WorkSearch : UserControl, ISearchUserControl
    {
        #region Fields

        /// <summary>
        /// The collection of works returned from the search.
        /// </summary>
        private Work[] mWorks;

        /// <summary>
        /// The collection of tracks appearances for the work.
        /// </summary>
        private TrackAppearance[] mTrackAppearances;

        /// <summary>
        /// The work search parameters.
        /// </summary>
        private WorkSearchParam mSearchParameters;

        /// <summary>
        /// The work search client.
        /// </summary>
        private WebClient mSearchClient;

        /// <summary>
        /// The track search client.
        /// </summary>
        private WebClient mTrackSearchClient;

        /// <summary>
        /// The work search query result ID.
        /// </summary>
        private Guid mQueryResultID;

        /// <summary>
        /// The work information search depth.
        /// </summary>
        private string mWorkDepth;

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
        /// Creates new WorkSearch UserControl.
        /// </summary>
        public WorkSearch()
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
            mSearchParameters = new WorkSearchParam();
            mSearchParameters.Depth = "Names;";

            // Clear the search results
            dtgWorkResults.ItemsSource = null;
            dtgTrackAppearances.ItemsSource = null;
            txtResponse.Text = String.Empty;
            pageNavigator.Visibility = Visibility.Hidden;
                        
            // Initialize the autocomplete fields
            InitAutoComplete();

            // Initialize the search depth text field
            mWorkDepth = "Publications;Names;";
            txtSearchDepth.Text = Util.GetSearchDepthTruncated(mWorkDepth);

            // Initialize the search URL text field 
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);

            // Register the page navigator event handler
            pageNavigator.PageChanged += PageChanged;

        }

        /// <summary>
        /// Initializes the AutoCompleteBox controls.
        /// </summary>
        public void InitAutoComplete()
        {
            Util.InitAutocompleteProperties(txtSearchName);
            Util.InitAutocompleteProperties(txtSearchCatalogue);
            Util.InitAutocompleteProperties(txtSearchComposers);
        }

        /// <summary>
        /// Gets the work search URL.
        /// </summary>
        /// <param name="searchParam">The work search parameters.</param>
        /// <returns>The work search URL.</returns>
        private string GetWorkSearchUrl(WorkSearchParam searchParam)
        {
            DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
            searchParam.Format = Settings.ResponseFormat.ToString().ToLower();
            Uri uri = urlBuilder.GetWorkSearchUrl(searchParam);
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

            if (mTrackSearchClient != null)
                mTrackSearchClient.CancelAsync();

            // Clear the search results
            dtgWorkResults.ItemsSource = null;
            dtgTrackAppearances.ItemsSource = null;
            mWorks = null;

            // Issue a request to search for works
            mSearchClient = new WebClient();
            Util.SetDecibelRequestHeaders(mSearchClient);
            mSearchClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(WorkSearch_Completed);
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
                String url = Settings.ApiAddress + "Works/Pages/" + mQueryResultID + "?pageNumber=" + (e.PageNumber - 1) + "&format=" + Settings.ResponseFormat;
                Search(new Uri(url));
            }
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void WorkSearch_Completed(object sender, DownloadStringCompletedEventArgs e)
        {        
            if(e.Error == null)
            {
                // Deserialize the response 
                WorkQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<WorkQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<WorkQueryResult>(e.Result);
                mWorks = queryResult.ResultSet;

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
                dataTable.Columns.Add("Composers", typeof(string));
                int count = 0;
                foreach (Work work in queryResult.ResultSet)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = count + 1;
                    row["Name"] = work.Name;
                    row["Composers"] = work.Composers;
                    dataTable.Rows.Add(row);
                    count++;
                }
                dtgWorkResults.IsReadOnly = true;
                dtgWorkResults.ItemsSource = dataTable.AsDataView();

                // Update the response text box
                mSetResponse = new BackgroundWorker();
                mSetResponse.DoWork += new DoWorkEventHandler(mSetResponse_DoWork);
                mSetResponse.RunWorkerAsync(e.Result);

                if (dtgWorkResults.Items.Count > 0)
                    Util.SetDatagridItem(dtgWorkResults, 0, 0);
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
        /// Occurs when a work is selected from the results table.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void dtgWorkResults_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // Refresh the track appearances
            DataRowView drv = (DataRowView)dtgWorkResults.SelectedItem;
            if (drv != null)
            {
                int sel = Int32.Parse(drv[0].ToString()) - 1;
                if (mWorks != null && sel > -1 && sel < mWorks.Length)
                    RefreshTrackAppearances(mWorks[sel]);
            }
        }

        /// <summary>
        /// Refreshes the track appearances.
        /// </summary>
        /// <param name="work">The Work object.</param>
        private void RefreshTrackAppearances(Work work)
        {
            // Clear the search results
            dtgTrackAppearances.ItemsSource = null;

            // Cancel any pending searches
            if (mTrackSearchClient != null)
                mTrackSearchClient.CancelAsync();

            if (mWorkDepth.Split(';').ToList().Contains("Publications"))
            {
                // Issue a request for works track appearances
                Uri uri = new Uri(Settings.ApiAddress + "Works/" + work.ID + "/Tracks" + "?format=" + Settings.ResponseFormat);
                mTrackSearchClient = new WebClient();
                Util.SetDecibelRequestHeaders(mTrackSearchClient);
                mTrackSearchClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(WorkTrackAppearances_Completed);
                mTrackSearchClient.DownloadStringAsync(uri);
                this.BeginSearch(this, new BeginSearchEventArgs() { Uri = uri });
            }
            else
                this.EndSearch(this, new EndSearchEventArgs() { ResultCount = dtgWorkResults.Items.Count });
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void WorkTrackAppearances_Completed(object sender, DownloadStringCompletedEventArgs e)
        {     
            if(e.Error == null)
            {
                // Deserialize the response 
                WorkTrackAppearanceQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<WorkTrackAppearanceQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<WorkTrackAppearanceQueryResult>(e.Result);
                mTrackAppearances = queryResult.ResultSet;

                // Initialize the results table
                DataTable dataTable = new DataTable();
                dataTable.Columns.Add("Index", typeof(int));
                dataTable.Columns.Add("TrackName", typeof(string));
                dataTable.Columns.Add("ArtistName", typeof(string));
                dataTable.Columns.Add("AlbumName", typeof(string));
                dataTable.Columns.Add("TrackNumber", typeof(string));
                dataTable.Columns.Add("TrackLength", typeof(string));
                int count = 0;
                foreach (TrackAppearance work in queryResult.ResultSet)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = count + 1;
                    row["TrackName"] = work.TrackName;
                    row["ArtistName"] = work.TrackArtistName;
                    row["AlbumName"] = work.AlbumName;
                    row["TrackNumber"] = work.TrackNumber;
                    row["TrackLength"] = Util.FormatTime(Convert.ToInt32(work.TotalSeconds));
                    dataTable.Rows.Add(row);
                    count++;
                }
                dtgTrackAppearances.IsReadOnly = true;
                dtgTrackAppearances.ItemsSource = dataTable.AsDataView();
            }

            if(!e.Cancelled)
                this.EndSearch(this, new EndSearchEventArgs() { ResultCount = dtgWorkResults.Items.Count });
        }

        /// <summary>
        /// Occurs when the depth button is clicked. 
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnSelectDepth_Click(object sender, RoutedEventArgs e)
        {
            SearchDepth winSearchDepth = new SearchDepth();
            winSearchDepth.ShowDialog(SearchDepth.GetWorkDepthParameters(), mWorkDepth.Split(';'));
            if(winSearchDepth.DialogResult == true)   
            {
                mWorkDepth = winSearchDepth.GetCheckedItems();
                txtSearchDepth.Text = Util.GetSearchDepthTruncated(mWorkDepth);
            }
        }

        /// <summary>
        /// Refreshes the search URL.
        /// </summary>
        public void RefreshSearchUrl()
        {
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Sets the response format.
        /// </summary>
        /// <param name="responseFormat">The response format.</param>
        public void SetResponseFormat(DecibelResponseFormat responseFormat)
        {
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);
            if (dtgWorkResults.Items.Count > 0)
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
            txtResponse.Dispatcher.Invoke((Action)delegate { txtResponse.Text = response; });
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
                if (e.UserState.Equals("Works") && suggestions.Works != null)
                {
                    foreach (Suggestion suggestion in suggestions.Works)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchName.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("MusicCatalogues") && suggestions.MusicCatalogues != null)
                {
                    foreach (Suggestion suggestion in suggestions.MusicCatalogues)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchCatalogue.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("WorksComposers") && suggestions.WorksComposers != null)
                {
                    foreach (Suggestion suggestion in suggestions.WorksComposers)
                        autocomplete.Add(suggestion.SuggestionValue);
                    this.txtSearchComposers.ItemsSource = autocomplete;
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

            if (mTrackSearchClient != null)
                mTrackSearchClient.Dispose();

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
                       (mTrackSearchClient != null && mTrackSearchClient.IsBusy);
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
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchName_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchName.Text, "Works");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchName_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchName.IsDropDownOpen)
                btnSearch_Click(sender, e);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchCatalogue_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Catalogue = txtSearchCatalogue.Text;
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchCatalogue_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchCatalogue.IsDropDownOpen)
                btnSearch_Click(sender, e);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchCatalogue_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchCatalogue.Text, "MusicCatalogues");
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchComposers_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Composers = txtSearchComposers.Text;
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchComposers_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchComposers.Text, "WorksComposers");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchComposers_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchComposers.IsDropDownOpen)
                btnSearch_Click(sender, e);
        }
         
        /// <summary>
        /// Occurs when the content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchDepth_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Depth = txtSearchDepth.Text;
            txtSearchUrl.Text = GetWorkSearchUrl(mSearchParameters);
        }

        #endregion KeyEvents
    }
}
