using System;
using System.IO;
using System.Net;
using System.Windows;
using System.Windows.Resources;

namespace DecibelRestWpf
{
    /// <summary>
    /// Displays album cover art.
    /// </summary>
    public partial class AlbumImage : Window
    {
        #region Constructors

        /// <summary>
        /// Creates new AlbumImage Window.
        /// </summary>
        /// <param name="imageUrl">The album image URL.</param>
        public AlbumImage(string imageUrl)
        {
            InitializeComponent();

            // Get animated loading gif from resources
            StreamResourceInfo streamResourceInfo = Application.GetResourceStream(new Uri(@"Resources/loading.gif", UriKind.Relative));
            if (streamResourceInfo != null)
            {
                Stream stream = streamResourceInfo.Stream;
                System.Drawing.Image image = System.Drawing.Image.FromStream(stream);
                this.picLoading.Image = image;
            }

            // Issue a request for the album image
            RequestAlbumImage(imageUrl);
        }

        #endregion Constructors

        #region Methods

        /// <summary>
        /// Issues a request for an album image.
        /// </summary>
        /// <param name="imageUrl">The album image URL.</param>
        private void RequestAlbumImage(string imageUrl)
        {
            WebClient client = new WebClient();
            Util.SetDecibelRequestHeaders(client);
            client.DownloadDataCompleted += new DownloadDataCompletedEventHandler(client_DownloadDataCompleted);
            client.DownloadDataAsync(new Uri(imageUrl));
        }
        
        /// <summary>
        /// Occurs when an asynchronous data download operation is completed.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void client_DownloadDataCompleted(object sender, DownloadDataCompletedEventArgs e)
        {
            imgAlbum.Source = Util.GetImageSourceFromByteArray(e.Result);
            wfiHost.Visibility = Visibility.Hidden;
            picLoading.Visible = false;
        }

        #endregion Methods
    }
}
