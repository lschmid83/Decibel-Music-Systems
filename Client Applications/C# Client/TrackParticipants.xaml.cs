using System;
using System.Data;
using System.Windows;

namespace DecibelRestWpf
{
    /// <summary>
    /// Reads a Decibel Track object and displays a list of track performance participations.
    /// </summary>
    public partial class TrackParticipants : Window
    {
        #region Constructors

        /// <summary>
        /// Creates new TrackParticipants Window.
        /// </summary>
        /// <param name="track">The Track object containing the participant information.</param>
        public TrackParticipants(Track track)
        {
            InitializeComponent();

            DataTable dataTable = new DataTable();
            dataTable.Columns.Add("Index", typeof(int));
            dataTable.Columns.Add("Name", typeof(string));
            dataTable.Columns.Add("Involvement", typeof(string));

            if (track.Performances != null && track.Performances.Length > 0)
            {
                int count = 0;
                foreach (Participation participation in track.Performances[0].Participations)
                {
                    DataRow row = dataTable.NewRow();
                    row["Index"] = count + 1;
                    row["Name"] = participation.Name.Split(',')[0];
                    row["Involvement"] = participation.Name.Split(',')[1];
                    dataTable.Rows.Add(row);
                    count++;
                }
            }
            
            dtgTrackParticipants.IsReadOnly = true;
            dtgTrackParticipants.ItemsSource = dataTable.AsDataView();
        }

        #endregion Constructors

        #region Events

        /// <summary>
        /// Occurs when the button is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnOK_Click(object sender, RoutedEventArgs e)
        {
            this.DialogResult = true;
            this.Close();
        }

        #endregion Events
    }
}