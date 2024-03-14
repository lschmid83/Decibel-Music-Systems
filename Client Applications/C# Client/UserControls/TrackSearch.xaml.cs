using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Net;
using System.Web.Script.Serialization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using DecibelRestWpf.Http;

namespace DecibelRestWpf.UserControls
{
    /// <summary>
    /// Searches for tracks using the Decibel REST API.
    /// </summary>
    public partial class TrackSearch : UserControl, ISearchUserControl
    {
        #region Fields

        /// <summary>
        /// The collection of tracks returned from the search.
        /// </summary>
        private Track[] mTracks;

        /// <summary>
        /// The selected album with specified retrieval depth.
        /// </summary>
        private Album mAlbum;

        /// <summary>
        /// The collection of tracks for the selected album.
        /// </summary>
        private Track[] mTrackList;

        /// <summary>
        /// The track search parameters.
        /// </summary>
        private TrackSearchParam mSearchParameters;

        /// <summary>
        /// The track search client. 
        /// </summary>
        private WebClient mSearchClient;

        /// <summary>
        /// The album information search client.
        /// </summary>
        private WebClient mAlbumClient;

        /// <summary>
        /// The track search query result ID.
        /// </summary>
        private Guid mQueryResultID;

        /// <summary>
        /// The track search depth.
        /// </summary>
        private string mTrackDepth;

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

        /// <summary>
        /// Occurs when the status text is updated.
        /// </summary>
        public event UpdateStatusEventHandler UpdateStatus;

        #endregion EventHandlers

        #region Constructors

        /// <summary>
        /// Creates new TrackSearch UserControl.
        /// </summary>
        public TrackSearch()
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
            mSearchParameters = new TrackSearchParam { Depth = "Names;" };

            // Clear the search results
            dtgTrackResults.ItemsSource = null;
            albumInformation.ClearAlbumInformation();
            lstTracks.Items.Clear();
            txtResponse.Text = String.Empty;
            pageNavigator.Visibility = Visibility.Hidden;

            // Initialize the autocomplete fields
            InitAutoComplete();

            // Initialize the search depth text field
            mTrackDepth = "Participations;ExternalIdentifiers;Genres;Names;Performances";
            txtSearchDepth.Text = Util.GetSearchDepthTruncated(mTrackDepth);

            // Initialize the search URL text field 
            txtSearchUrl.Text = GetTrackSearchUrl(mSearchParameters);

            // Register the album information disc changed event handler
            albumInformation.DiscChanged += albumInformation_DiscChanged;

            // Register the page navigator event handler
            pageNavigator.PageChanged += PageChanged;
        }

        /// <summary>
        /// Initializes the AutoCompleteBox controls.
        /// </summary>
        public void InitAutoComplete()
        {
            Util.InitAutocompleteProperties(txtSearchTrackTitle);
            Util.InitAutocompleteProperties(txtSearchArtist);
            Util.InitAutocompleteProperties(txtSearchAlbumTitle);
        }

        /// <summary>
        /// Gets the track search URL.
        /// </summary>
        /// <param name="searchParam">The track search parameters.</param>
        /// <returns>The track search URL.</returns>
        private string GetTrackSearchUrl(TrackSearchParam searchParam)
        {
            DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
            searchParam.Format = Settings.ResponseFormat.ToString().ToLower();
            Uri uri = urlBuilder.GetTrackSearchUrl(searchParam);
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

            if (mAlbumClient != null)
                mAlbumClient.CancelAsync();

            // Clear the search results
            dtgTrackResults.ItemsSource = null;
            albumInformation.ClearAlbumInformation();
            lstTracks.Items.Clear();
            mTracks = null;

            // Issue a request to search for tracks
            mSearchClient = new WebClient();
            Util.SetDecibelRequestHeaders(mSearchClient);
            mSearchClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(TrackSearch_Completed);
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
                String url = Settings.ApiAddress + "Tracks/Pages/" + mQueryResultID + "?pageNumber=" + (e.PageNumber - 1) + "&format=" + Settings.ResponseFormat;
                Search(new Uri(url));
            }
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void TrackSearch_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            if(e.Error == null)
            {
                // Deserialize the response 
                TrackQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<TrackQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<TrackQueryResult>(e.Result);
                mTracks = queryResult.ResultSet;

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
                dataTable.Columns.Add("Artists", typeof(string));
                dataTable.Columns.Add("SequenceNumber", typeof(string));
                dataTable.Columns.Add("TrackLength", typeof(string));
                int count = 0;
                foreach (Track track in mTracks)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = count + 1;
                    row["Name"] = track.Name;
                    row["Artists"] = track.Artists;
                    row["SequenceNumber"] = track.SequenceNo;
                    row["TrackLength"] = Util.FormatTime(Convert.ToInt32(track.TotalSeconds));
                    dataTable.Rows.Add(row);
                    count++;
                }
                dtgTrackResults.IsReadOnly = true;
                dtgTrackResults.ItemsSource = dataTable.AsDataView();

                // Update the response text box
                mSetResponse = new BackgroundWorker();
                mSetResponse.DoWork += new DoWorkEventHandler(mSetResponse_DoWork);
                mSetResponse.RunWorkerAsync(e.Result);

                if (dtgTrackResults.Items.Count > 0)
                    Util.SetDatagridItem(dtgTrackResults, 0, 0);
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
        /// Occurs when a track is selected from the results table.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void dtgTrackResults_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // Refresh the album information
            DataRowView drv = (DataRowView)dtgTrackResults.SelectedItem;
            if (drv != null)
            {
                int sel = Int32.Parse(drv[0].ToString()) - 1;
                if (mTracks != null && sel > -1 && sel < mTracks.Length)
                    RefreshAlbumInformation(mTracks[sel]);
            }
        }

        /// <summary>
        /// Requests album information.
        /// </summary>
        /// <param name="track">The Track object.</param>
        private void RefreshAlbumInformation(Track track)
        {
            // Clear the search results
            albumInformation.ClearAlbumInformation();
            lstTracks.Items.Clear();

            // Cancel any pending searches
            if (mAlbumClient != null)
                mAlbumClient.CancelAsync();

            // Issue a request for album information
            Uri uri = new Uri(Settings.ApiAddress + "Albums/" + "?depth=" + mTrackDepth + ";Tracks;Publications;Media;ImageThumbnail;&id=" + track.AlbumID + "&format=" + Settings.ResponseFormat);
            mAlbumClient = new WebClient();
            Util.SetDecibelRequestHeaders(mAlbumClient);
            mAlbumClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(AlbumInformation_Completed);
            mAlbumClient.DownloadStringAsync(uri);
            this.BeginSearch(this, new BeginSearchEventArgs() { Uri = uri });
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void AlbumInformation_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            if(e.Error == null)
            {
                // Deserialize the response 
                AlbumQueryResult queryResult;
                if (Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<AlbumQueryResult>(e.Result);
                else
                    queryResult = new JavaScriptSerializer().Deserialize<AlbumQueryResult>(e.Result);
                mAlbum = queryResult.ResultSet[0];
                albumInformation.SetAlbumInformation(mAlbum);
            }
            
            if(!e.Cancelled)
                this.EndSearch(this, new EndSearchEventArgs() { ResultCount = dtgTrackResults.Items.Count });
        }

        /// <summary>
        /// Occurs when a mouse button is clicked two or more times.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void lstTracks_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (lstTracks.Items.Count > 0)
            {
                // Display the track participants in a new window
                TrackParticipants winTrackParticipants = new TrackParticipants(mTrackList[lstTracks.SelectedIndex]);
                winTrackParticipants.ShowDialog();
            }
        }

        /// <summary>
        /// Occurs when the mouse pointer enters the bounds of this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void lstTracks_MouseEnter(object sender, MouseEventArgs e)
        {
            if (!IsBusy && lstTracks.Items.Count > 0)
                this.UpdateStatus(this, new UpdateStatusEventArgs() { StatusText = "Double click on a track to view participants information" });
        }

        /// <summary>
        /// Occurs when the mouse pointer leaves the bounds of this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void lstTracks_MouseLeave(object sender, MouseEventArgs e)
        {
            if (!IsBusy)
                this.UpdateStatus(this, new UpdateStatusEventArgs() { StatusText = "Ready" });
        }

        /// <summary>
        /// Occurs when a disc is selected.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void albumInformation_DiscChanged(object sender, AlbumInformation.DiscChangedEventArgs e)
        {
            RefreshTrackInformation(mAlbum, e.DiscNumber);
        }

        /// <summary>
        /// Refreshes the track information for the album and disc number.
        /// </summary>
        /// <param name="album">The Album object.</param>
        /// <param name="discNumber">The disc number.</param>
        private void RefreshTrackInformation(Album album, string discNumber)
        {
            lstTracks.Items.Clear();
            if (album.Tracks != null)
            {
                // Initialize the track list
                mTrackList = album.Tracks;
                foreach (Track track in album.Tracks)
                {
                    if (track.DiscNumber.Equals(discNumber))
                    {
                        string trackInfo = track.SequenceNo + ") " + track.Name;
                        if (track.TotalSeconds > 0)
                            trackInfo += " (" + Util.FormatTime(Convert.ToInt32(track.TotalSeconds)) + ")";
                        lstTracks.Items.Add(trackInfo);
                    }
                }
            }
        }

        /// <summary>
        /// Occurs when the depth button is clicked. 
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnSelectDepth_Click(object sender, RoutedEventArgs e)
        {
            SearchDepth winSearchDepth = new SearchDepth();
            winSearchDepth.ShowDialog(SearchDepth.GetTrackDepthParameters(), mTrackDepth.Split(';'));
            if(winSearchDepth.DialogResult == true)   
            {
                mTrackDepth = winSearchDepth.GetCheckedItems();
                txtSearchDepth.Text = Util.GetSearchDepthTruncated(mTrackDepth);
            }
        }

        /// <summary>
        /// Refreshes the search URL.
        /// </summary>
        public void RefreshSearchUrl()
        {
            txtSearchUrl.Text = GetTrackSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Sets the response format.
        /// </summary>
        /// <param name="responseFormat">The response format.</param>
        public void SetResponseFormat(DecibelResponseFormat responseFormat)
        {
            txtSearchUrl.Text = GetTrackSearchUrl(mSearchParameters);
            if (dtgTrackResults.Items.Count > 0)
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
                if (e.UserState.Equals("Tracks") && suggestions.Tracks != null)
                {
                    foreach (Suggestion suggestion in suggestions.Tracks)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchTrackTitle.ItemsSource = autocomplete;  
                }
                else if (e.UserState.Equals("Participants") && suggestions.Participants != null)
                {
                    foreach (Suggestion suggestion in suggestions.Participants)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchArtist.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("Albums") && suggestions.Albums != null)
                {
                    foreach (Suggestion suggestion in suggestions.Albums)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchAlbumTitle.ItemsSource = autocomplete;
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

            if (mAlbumClient != null)
                mAlbumClient.Dispose();

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
                       (mAlbumClient != null && mAlbumClient.IsBusy);
            }
        }

        #endregion Properties

        #region KeyEvents

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchTrackTitle_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.TrackTitle = txtSearchTrackTitle.Text;
            txtSearchUrl.Text = GetTrackSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchTrackTitle_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchTrackTitle.Text, "Tracks");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchTrackTitle_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchTrackTitle.IsDropDownOpen)
                btnSearch_Click(this, null);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchArtist_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Artist = txtSearchArtist.Text;
            txtSearchUrl.Text = GetTrackSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchArtist_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchArtist.IsDropDownOpen)
                btnSearch_Click(this, null);
        }
        
        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchArtist_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchArtist.Text, "Participants");
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchAlbumTitle_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.AlbumTitle = txtSearchAlbumTitle.Text;
            txtSearchUrl.Text = GetTrackSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchAlbumTitle_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchAlbumTitle.Text, "Albums");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchAlbumTitle_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchAlbumTitle.IsDropDownOpen)
                btnSearch_Click(this, null);
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void lstTracks_KeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter)
            {
                if (lstTracks.Items.Count > 0)
                {
                    // Display the track participants in a new window
                    TrackParticipants winTrackParticipants = new TrackParticipants(mTrackList[lstTracks.SelectedIndex]);
                    winTrackParticipants.ShowDialog();
                }
                e.Handled = true;
            }
        }

        #endregion KeyEvents
    }
}
