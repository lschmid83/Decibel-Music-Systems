using System;
using System.Windows;
using System.Windows.Documents;

namespace DecibelRestWpf
{
    /// <summary>
    /// Dialog box for entering Decibel API address, Application ID and Key.
    /// </summary>
    public partial class OptionsWindow : Window
    {
        #region Constructors

        /// <summary>
        /// Creates new OptionsWindow.
        /// </summary>
        public OptionsWindow()
        {
            InitializeComponent();

            txtApiAddress.Text = Settings.ApiAddress;
            txtApplicationID.Text = Settings.ApplicationID;
            txtApplicationKey.Text = Settings.ApplicationKey;
        }

        #endregion Constructors

        #region Events

        /// <summary>
        /// Occurs when the signup hyperlink is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void Signup_Click(object sender, RoutedEventArgs e)
        {
            Hyperlink source = sender as Hyperlink;
            if (source != null)
                System.Diagnostics.Process.Start(source.NavigateUri.ToString());
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
        /// Occurs when the OK button is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnOK_Click(object sender, RoutedEventArgs e)
        {
            Settings.ApiAddress = txtApiAddress.Text;
            try
            {
                Uri uri = new Uri(Settings.ApiAddress);
            }
            catch
            {
                Settings.ApiAddress = Settings.DefaultApiAddress;
            }
            Settings.ApplicationID = txtApplicationID.Text;
            Settings.ApplicationKey = txtApplicationKey.Text;
            Settings.WriteApplicationSettings();
            this.DialogResult = true;
            this.Close();
        }

        #endregion Events
    }
}
