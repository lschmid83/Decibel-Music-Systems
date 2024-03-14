using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;

namespace DecibelRestWpf
{
    /// <summary>
    /// Dialog box for selecting the retrieval depth.
    /// </summary>
    public partial class SearchDepth : Window
    {
        #region Fields

        /// <summary>
        /// The checked list of search depths.
        /// </summary>
        public ObservableCollection<CheckedListItem> DepthList { get; set; }

        #endregion Fields

        #region Methods

        /// <summary>
        /// Shows the search depth dialog.
        /// </summary>
        /// <param name="searchDepths">The list of search depths.</param>
        /// <param name="selectedDepths">A string array of checked search depths.</param>
        public void ShowDialog(List<string> searchDepths, string[] selectedDepths)
        {
            InitializeComponent();

            DepthList = new ObservableCollection<CheckedListItem>();
           
            int index = 0;
            foreach (string suggestion in searchDepths)
            {
                DepthList.Add(new CheckedListItem { Text = suggestion, Checked = false, Index = index });
                index++;
            }

            // Set the checked list items 
            foreach (string depth in selectedDepths)
            {
                foreach (CheckedListItem cli in DepthList)
                {
                    if (depth.Equals(cli.Text))
                        cli.Checked = true;
                }
            }

            this.DataContext = this;
            this.ShowDialog();
        }
        
        /// <summary>
        /// Gets a string containing the checked search depth values.
        /// </summary>
        /// <returns>The checked search depth values.</returns>
        public String GetCheckedItems() {
            
            // Add the checked list items to the return value
            string selection = "";
            foreach (CheckedListItem cli in DepthList)
            {
                if (cli.Checked)
                    selection += cli.Text + ";";
            }
            return selection;
        }

        /// <summary>
        /// Gets a list of the album depth parameters.
        /// </summary>
        /// <returns>The list of album depth parameters.</returns>
        public static List<string> GetAlbumDepthParameters()
        {
            List<string> albumDepth = new List<string>();
            albumDepth.Add("Tracks");
            albumDepth.Add("ImageThumbnail");
            albumDepth.Add("Media");
            albumDepth.Add("Names");
            albumDepth.Add("ExternalIdentifiers");
            albumDepth.Add("Genres");
            albumDepth.Add("Publications");
            return albumDepth;
        }

        /// <summary>
        /// Gets a list of the participant depth parameters.
        /// </summary>
        /// <returns>The list of participant depth parameters.</returns>
        public static List<string> GetParticipantDepthParameters()
        {
            List<string> participantDepth = new List<string>();
            participantDepth.Add("Relationships");
            participantDepth.Add("GroupMembers");
            participantDepth.Add("Nationalities");
            participantDepth.Add("Names");
            participantDepth.Add("GeographicAreas");
            participantDepth.Add("Annotations");
            participantDepth.Add("ChartsAwards");
            return participantDepth;
        }

        /// <summary>
        /// Gets a list of the track depth parameters.
        /// </summary>
        /// <returns>The list of track depth parameters.</returns>
        public static List<string> GetTrackDepthParameters()
        {
            List<string> trackDepth = new List<string>();
            trackDepth.Add("Participations");
            trackDepth.Add("ExternalIdentifiers");
            trackDepth.Add("Genres");
            trackDepth.Add("Names");
            trackDepth.Add("Performances");
            return trackDepth;
        }

        /// <summary>
        /// Gets a list of the work depth parameters.
        /// </summary>
        /// <returns>The list of work depth parameters.</returns>
        public static List<string> GetWorkDepthParameters()
        {
            List<string> trackDepth = new List<string>();
            trackDepth.Add("Publications");
            trackDepth.Add("Annotations");
            trackDepth.Add("ChartsAwards");
            trackDepth.Add("Names");
            trackDepth.Add("Genres");
            trackDepth.Add("Movements");
            trackDepth.Add("Nationalities");
            trackDepth.Add("Publishers");
            return trackDepth;
        }

        #endregion Methods

        #region Events

        /// <summary>
        /// Occurs when the OK button is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnOK_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = true;
            this.Close();
        }

        /// <summary>
        /// Occurs when the Cancel button is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnCancel_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = false;
            this.Close();
        }

        /// <summary>
        /// Occurs when a search depth is checked in the list box.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void chkDepth_Checked(object sender, RoutedEventArgs e)
        {
            // Set the list box selected item to the index of the checked item
            CheckBox chkGenre = (CheckBox)sender;
            lstDepth.SelectedIndex = Int32.Parse(chkGenre.Tag.ToString());
        }

        /// <summary>
        /// Occurs when a search depth is unchecked in the list box.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void chkDepth_Unchecked(object sender, RoutedEventArgs e)
        {
            CheckBox chkGenre = (CheckBox)sender;
            lstDepth.SelectedIndex = Int32.Parse(chkGenre.Tag.ToString());
        }

        #endregion Events

        #region InnerClasses

        /// <summary>
        /// Represents a checked list item.
        /// </summary>
        public class CheckedListItem
        {
            public string Text { get; set; }
            public bool Checked { get; set; }
            public int Index { get; set; }
        }

        #endregion InnerClasses

    }
}
