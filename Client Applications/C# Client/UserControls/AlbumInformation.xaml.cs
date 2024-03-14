using System;
using System.Collections.Generic;
using System.Windows.Controls;
using System.Windows.Input;

namespace DecibelRestWpf.UserControls
{
    /// <summary>
    /// Reads a Decibel Album object and displays album information.
    /// </summary>
    public partial class AlbumInformation : UserControl
    {
        #region Fields

        /// <summary>
        /// Stores the the album image URL.
        /// </summary>
        private string mImageUrl;

        /// <summary>
        /// The selected disc index.
        /// </summary>
        private int mSelectedDisc;

        #endregion Fields

        #region EventHandlers

        /// <summary>
        /// Occurs when a disc is selected.
        /// </summary>
        public event DiscChangedEventHandler DiscChanged;

        /// <summary>
        /// Custom event handler for DiscChanged event.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        public delegate void DiscChangedEventHandler(object sender, DiscChangedEventArgs e);

        /// <summary>
        /// Custom event arguments for DiscChanged event.
        /// </summary>
        public class DiscChangedEventArgs: EventArgs
        {
            public string DiscNumber;
        }
        
        #endregion EventHandlers

        #region Constructors

        /// <summary>
        /// Creates new AlbumInformation UserControl.
        /// </summary>
        public AlbumInformation()
        {
            InitializeComponent();
        }

        #endregion Constructors

        #region Methods

        /// <summary>
        /// Clears the album information fields.
        /// </summary>
        public void ClearAlbumInformation()
        {
            txtAlbumName.Text = String.Empty;
            txtAlbumArtists.Text = String.Empty;
            cmbAlbumDiscs.Items.Clear();
            txtAlbumPublisher.Text = String.Empty;
            txtAlbumCatalogue.Text = String.Empty;
            cmbAlbumDiscs.IsEnabled = false;
            txtAlbumTracks.Text = String.Empty;
            txtAlbumDuration.Text = String.Empty;
            txtAlbumFormat.Text = String.Empty;
            txtAlbumReleaseDate.Text = String.Empty;
            txtAlbumRegion.Text = String.Empty;
            txtAlbumGenres.Text = String.Empty;
            txtAlbumBarcode.Text = String.Empty;
            imgAlbumThumbnail.Source = null;
        }
        
        /// <summary>
        /// Sets the album information.
        /// </summary>
        /// <param name="album">The Album object.</param>
        public void SetAlbumInformation(Album album)
        {
            ClearAlbumInformation();

            // Album name
            txtAlbumName.Text = album.Name;

            // Artists
            txtAlbumArtists.Text = album.Artists;

            // Discs
            for (int i = 1; i <= album.DiscCount; i++)
                cmbAlbumDiscs.Items.Add(i);
            if (cmbAlbumDiscs.Items.Count > 0)
            {
                cmbAlbumDiscs.IsEnabled = true;
                cmbAlbumDiscs.SelectedIndex = 0;
            }

            // Number of tracks
            txtAlbumTracks.Text = album.TrackCount.ToString();

            // Duration
            txtAlbumDuration.Text = Util.FormatTime(Convert.ToInt32(album.TotalSeconds));

            // Format
            if(album.Discs != null && album.Discs.Length > 0 && album.Discs[mSelectedDisc].MusicMedium != null)
                txtAlbumFormat.Text = album.Discs[mSelectedDisc].MusicMedium.Name;

            // Release date
            if(album.ReleaseDate != null && !String.IsNullOrWhiteSpace(album.ReleaseDate.Name))
                txtAlbumReleaseDate.Text = album.ReleaseDate.Name;

            // Catalogue number
            if (album.Publications != null && album.Publications.Length > 0)
                txtAlbumCatalogue.Text = album.Publications[0].CatalogueNumber;
    
            // Label
            List<string> publishers = Util.GetAlbumPublishers(album);
            if (publishers.Count > 0)
                txtAlbumPublisher.Text = publishers[0];
    
            // Genres 
            if (album.Genres != null)
            {
                foreach (GenreValue genreValue in album.Genres)
                {
                    Genre genre = genreValue.Genre;
                    if (!String.IsNullOrWhiteSpace(genre.Name))
                        txtAlbumGenres.Text += genre.Name + ", ";
                }
                if (txtAlbumGenres.Text.Length > 2)
                    txtAlbumGenres.Text = txtAlbumGenres.Text.Substring(0, txtAlbumGenres.Text.Length - 2);
            }

            // Region
            if (album.GeoEntities != null)
            {
                foreach (GeoEntityValue gev in album.GeoEntities)
                {
                    GeoEntity ge = gev.GeoEntity;
                    if (!String.IsNullOrWhiteSpace(ge.Name))
                    {
                        txtAlbumRegion.Text = txtAlbumRegion.Text + ge.Name + ", ";
                    }
                }
                if (txtAlbumRegion.Text.Length > 2)
                    txtAlbumRegion.Text = txtAlbumRegion.Text.Substring(0, txtAlbumRegion.Text.Length - 2);
            }

            // Barcode
            List<string> barcodes = Util.GetAlbumBarcodes(album);
            if (barcodes.Count > 0)
                txtAlbumBarcode.Text = barcodes[0];

            // Thumbnail image
            imgAlbumThumbnail.Source = Util.GetImageSourceFromByteArray(album.Thumbnail);
            mImageUrl = album.ImageUrl.Url;
        }

        #endregion Methods

        #region Events

        /// <summary>
        /// Occurs when a disc is selected.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void cmbAlbumDisc_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (cmbAlbumDiscs.SelectedValue != null)
            {
                this.DiscChanged(this, new DiscChangedEventArgs() { DiscNumber = cmbAlbumDiscs.SelectedValue.ToString() });
                mSelectedDisc = cmbAlbumDiscs.SelectedIndex;
            }
        }

        /// <summary>
        /// Occurs when any mouse button is pressed while the pointer is over this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void imgAlbumThumbnail_MouseDown(object sender, MouseButtonEventArgs e)
        {
            if (!String.IsNullOrWhiteSpace(mImageUrl))
            {
                AlbumImage winAlbumImage = new AlbumImage(mImageUrl);
                winAlbumImage.ShowDialog();
            }
        }

        /// <summary>
        /// Occurs when the mouse pointer enters the bounds of this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void imgAlbumThumbnail_MouseEnter(object sender, MouseEventArgs e)
        {
            Mouse.OverrideCursor = Cursors.Hand;
        }

        /// <summary>
        /// Occurs when the mouse pointer leaves the bounds of this element.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void imgAlbumThumbnail_MouseLeave(object sender, MouseEventArgs e)
        {
            Mouse.OverrideCursor = null;
        }

        #endregion Events
    }
}
