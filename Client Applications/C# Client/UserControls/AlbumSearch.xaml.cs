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
    /// Searches for albums using the Decibel REST API.
    /// </summary>
    public partial class AlbumSearch : UserControl, ISearchUserControl
    {
        #region Fields

        /// <summary>
        /// The collection of albums returned from the search.
        /// </summary>
        private Album[] mAlbums;

        /// <summary>
        /// The selected album with specified retrieval depth.
        /// </summary>
        private Album mAlbum;

        /// <summary>
        /// The track information for the selected album.
        /// </summary>
        private Track[] mTrackList;

        /// <summary>
        /// The album search parameters.
        /// </summary>
        private AlbumSearchParam mSearchParameters;

        /// <summary>
        /// The album search client. 
        /// </summary>
        private WebClient mSearchClient;

        /// <summary>
        /// The album information search client.
        /// </summary>
        private WebClient mAlbumClient;

        /// <summary>
        /// The album search query result ID.
        /// </summary>
        private Guid mQueryResultID;

        /// <summary>
        /// The album information search depth.
        /// </summary>
        private string mAlbumDepth;

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
        /// Creates new AlbumSearch UserControl.
        /// </summary>
        public AlbumSearch()
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
            mSearchParameters = new AlbumSearchParam();
            mSearchParameters.Depth = "Publications;";

            // Clear the search results
            dtgAlbumResults.ItemsSource = null;
            albumInformation.ClearAlbumInformation();
            lstTracks.Items.Clear();
            txtResponse.Text = String.Empty;
            pageNavigator.Visibility = Visibility.Hidden;

            // Initialize the autocomplete fields
            InitAutoComplete();

            // Initialize the search depth text field
            mAlbumDepth = "Tracks;ImageThumbnail;Media;Names;ExternalIdentifiers;Genres;Publications;";
            txtSearchDepth.Text = Util.GetSearchDepthTruncated(mAlbumDepth);

            // Initialize the search URL text field 
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);

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
            Util.InitAutocompleteProperties(txtSearchAlbumTitle);
            Util.InitAutocompleteProperties(txtSearchBarcode);
            Util.InitAutocompleteProperties(txtSearchArtistName);
            Util.InitAutocompleteProperties(txtSearchLabel);
            Util.InitAutocompleteProperties(txtSearchGenre);
        }

        /// <summary>
        /// Gets the album search URL.
        /// </summary>
        /// <param name="searchParam">The album search parameters.</param>
        /// <returns>The album search URL.</returns>
        public string GetAlbumSearchUrl(AlbumSearchParam searchParam)
        {
            DecibelUrlBuilder urlBuilder = new DecibelUrlBuilder(Settings.ApiAddress);
            searchParam.Format = Settings.ResponseFormat.ToString().ToLower();
            Uri uri = urlBuilder.GetAlbumSearchUrl(searchParam);
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
            dtgAlbumResults.ItemsSource = null;
            albumInformation.ClearAlbumInformation();
            lstTracks.Items.Clear();
            mAlbums = null;

            // Issue a request to search for albums
            mSearchClient = new WebClient();
            Util.SetDecibelRequestHeaders(mSearchClient);
            mSearchClient.DownloadStringCompleted += new DownloadStringCompletedEventHandler(AlbumSearch_Completed);
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
                String url = Settings.ApiAddress + "Albums/Pages/" + mQueryResultID.ToString() + "?pageNumber=" + (e.PageNumber - 1) + "&format=" + Settings.ResponseFormat;
                Search(new Uri(url));
            }
        }

        /// <summary>
        /// Occurs when an asynchronous resource-download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void AlbumSearch_Completed(object sender, DownloadStringCompletedEventArgs e)
        {
            
            if(e.Error == null)
            {
                // Deserialize the response 
                AlbumQueryResult queryResult;
                if(Settings.ResponseFormat == DecibelResponseFormat.XML)
                    queryResult = Util.DeserializeXmlString<AlbumQueryResult>(e.Result);
                else
                     queryResult = new JavaScriptSerializer().Deserialize<AlbumQueryResult>(e.Result);
                mAlbums = queryResult.ResultSet;
                
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
                dataTable.Columns.Add("Label", typeof(string));
                dataTable.Columns.Add("Length", typeof(string));
                dataTable.Columns.Add("TrackCount", typeof(int));
                int count = 0;
                foreach (Album album in mAlbums)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = count + 1;
                    row["Name"] = album.Name;
                    row["Artists"] = album.Artists;
                    List<string> publishers = Util.GetAlbumPublishers(album);
                    row["Label"] = publishers.Count > 0 ? publishers[0] : null;
                    row["Length"] = Util.FormatTime(Convert.ToInt32(album.TotalSeconds));
                    row["TrackCount"] = album.TrackCount;
                    dataTable.Rows.Add(row);
                    count++;
                }
                dtgAlbumResults.IsReadOnly = true;
                dtgAlbumResults.ItemsSource = dataTable.AsDataView();

                // Update the response text box
                mSetResponse = new BackgroundWorker();
                mSetResponse.DoWork += new DoWorkEventHandler(mSetResponse_DoWork);
                mSetResponse.RunWorkerAsync(e.Result);

                if (dtgAlbumResults.Items.Count > 0)
                    Util.SetDatagridItem(dtgAlbumResults, 0, 0);
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
        /// Occurs when an album is selected from the results table.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void dtgAlbumResults_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            // Refresh the album information
            DataRowView drv = (DataRowView)dtgAlbumResults.SelectedItem;
            if (drv != null)
            {
                int sel = Int32.Parse(drv[0].ToString()) - 1;
                if (mAlbums != null && sel > -1 && sel < mAlbums.Length)
                    RefreshAlbumInformation(mAlbums[sel]);
            }
        }

        /// <summary>
        /// Refreshes the album information.
        /// </summary>
        /// <param name="album">The Album object.</param>
        private void RefreshAlbumInformation(Album album)
        {
            // Clear the search results
            albumInformation.ClearAlbumInformation();
            lstTracks.Items.Clear();

            // Cancel any pending searches
            if (mAlbumClient != null)
                mAlbumClient.CancelAsync();

            // Issue a request for album information
            Uri uri = new Uri(Settings.ApiAddress + "Albums/" + "?depth=" + mAlbumDepth + "Performances;&id=" + album.ID + "&format=" + Settings.ResponseFormat);
            Console.WriteLine(uri.ToString());
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

            if (!e.Cancelled)
                this.EndSearch(this, new EndSearchEventArgs() { ResultCount = dtgAlbumResults.Items.Count });
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
            winSearchDepth.ShowDialog(SearchDepth.GetAlbumDepthParameters(), mAlbumDepth.Split(';'));
            if(winSearchDepth.DialogResult == true)   
            {
                mAlbumDepth = winSearchDepth.GetCheckedItems();
                txtSearchDepth.Text = Util.GetSearchDepthTruncated(mAlbumDepth);
            }
        }

        /// <summary>
        /// Refreshes the search URL.
        /// </summary>
        public void RefreshSearchUrl()
        {
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Sets the response format.
        /// </summary>
        /// <param name="responseFormat">The response format.</param>
        public void SetResponseFormat(DecibelResponseFormat responseFormat)
        {
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
            if (dtgAlbumResults.Items.Count > 0)
                Search(new Uri(txtSearchUrl.Text));
            tabResponse.Header = responseFormat.ToString();
        }

        /// <summary>
        /// Gets the response string.
        /// </summary>
        /// <returns>The XML or JSON response string.</returns>
        public string GetResponse()
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
                if (e.UserState.Equals("Albums") && suggestions.Albums != null)
                {
                    foreach (Suggestion suggestion in suggestions.Albums)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchAlbumTitle.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("Barcodes") && suggestions.Barcodes != null)
                {
                    foreach (Suggestion suggestion in suggestions.Barcodes)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchBarcode.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("Participants") && suggestions.Participants != null)
                {
                    foreach (Suggestion suggestion in suggestions.Participants)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchArtistName.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("Publishers") && suggestions.Publishers != null)
                {
                    foreach (Suggestion suggestion in suggestions.Publishers)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchLabel.ItemsSource = autocomplete;
                }
                else if (e.UserState.Equals("MusicGenres") && suggestions.MusicGenres != null)
                {
                    foreach (Suggestion suggestion in suggestions.MusicGenres)
                        autocomplete.Add(suggestion.SuggestionValue);
                    txtSearchGenre.ItemsSource = autocomplete;
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
        private void txtSearchAlbumTitle_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.AlbumTitle = txtSearchAlbumTitle.Text;
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
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
                btnSearch_Click(sender, e);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchBarcode_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Barcode = txtSearchBarcode.Text;
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchBarcode_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchBarcode.Text, "Barcodes");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchBarcode_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchBarcode.IsDropDownOpen)
                btnSearch_Click(sender, e);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchArtistName_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.ArtistName = txtSearchArtistName.Text;
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchArtistName_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchArtistName.Text, "Participants");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchArtistName_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchArtistName.IsDropDownOpen)
                btnSearch_Click(sender, e);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchGenre_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Genre = txtSearchGenre.Text;
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchGenre_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchGenre.Text, "MusicGenres");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchGenre_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchGenre.IsDropDownOpen)
                btnSearch_Click(sender, e);
        }

        /// <summary>
        /// Occurs when content changes in the text element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchLabel_TextChanged(object sender, RoutedEventArgs e)
        {
            mSearchParameters.Label = txtSearchLabel.Text;
            txtSearchUrl.Text = GetAlbumSearchUrl(mSearchParameters);
        }

        /// <summary>
        /// Occurs when a key is released while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchLabel_KeyUp(object sender, KeyEventArgs e)
        {
            if (e.Key != Key.Down && e.Key != Key.Up)
                SendAutoCompleteRequest(txtSearchLabel.Text, "Publishers");
        }

        /// <summary>
        /// Occurs when a key is pressed while focus is on this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void txtSearchLabel_PreviewKeyDown(object sender, KeyEventArgs e)
        {
            if (e.Key == Key.Enter && !txtSearchLabel.IsDropDownOpen)
                btnSearch_Click(sender, e);
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

        /// <summary>
        /// Responds to key presses.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void PreviewTextInputHandler(Object sender, TextCompositionEventArgs e)
        {
            e.Handled = !IsNumeric(e.Text);
        }

        /// <summary>
        /// Determine whether a string is a valid representation of a numeric type.
        /// </summary>
        /// <param name="text">The text to parse.</param>
        /// <returns>True if the string contains a numeric value; otherwise, false.</returns>
        public static bool IsNumeric(string text)
        {
            return Array.TrueForAll<Char>(text.ToCharArray(),
                                          c => Char.IsDigit(c) || Char.IsControl(c));
        }

        /// <summary>
        /// Handles paste events for numeric only AutoComplete controls.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void PastingHandler(object sender, DataObjectPastingEventArgs e)
        {
            if (e.DataObject.GetDataPresent(typeof(string)))
            {
                string text = (string)e.DataObject.GetData(typeof(string));
                if (!IsNumeric(text)) e.CancelCommand();
            }
            else e.CancelCommand();
        }

        #endregion KeyEvents
    }
}
