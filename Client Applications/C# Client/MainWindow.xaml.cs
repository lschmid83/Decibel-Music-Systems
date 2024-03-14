using System;
using System.Diagnostics;
using System.Drawing;
using System.Windows;

namespace DecibelRestWpf
{
    /// <summary>
    /// The main application window.
    /// </summary>
    public partial class MainWindow : Window
    {
        #region Constructors

        /// <summary>
        /// Creates new MainWindow.
        /// </summary>
        public MainWindow()
        {
            InitializeComponent();
            RegisterEventHandlers();

            // Restore the size, state and location of the window
            RestoreWindowState();

            // Size the window to fit the current screen
            SizeToFit();

            // Move the window into view
            MoveIntoView();
            
            // Read the application settings
            Settings.ReadApplicationSettings();
            if (String.IsNullOrWhiteSpace(Settings.ApiAddress) || String.IsNullOrWhiteSpace(Settings.ApplicationID) || String.IsNullOrWhiteSpace(Settings.ApplicationKey))
                new OptionsWindow().ShowDialog();
            
            tbcSearchTabs.SelectedIndex = 0;
            SetResponseFormat(Settings.ResponseFormat);
        }

        #endregion Constructors

        #region Methods

        /// <summary>
        /// Registers the event handlers which update the status information.
        /// </summary>
        private void RegisterEventHandlers()
        {
            this.tabAlbums.BeginSearch += new BeginSearchEventHandler(BeginSearch);
            this.tabAlbums.EndSearch += new EndSearchEventHandler(EndSearch);
            this.tabAlbums.UpdateStatus += new UpdateStatusEventHandler(UpdateStatus);
            
            this.tabParticipants.BeginSearch += new BeginSearchEventHandler(BeginSearch);
            this.tabParticipants.EndSearch += new EndSearchEventHandler(EndSearch);
            
            this.tabTracks.BeginSearch += new BeginSearchEventHandler(BeginSearch);
            this.tabTracks.EndSearch += new EndSearchEventHandler(EndSearch);
            this.tabTracks.UpdateStatus += UpdateStatus;
            
            this.tabWorks.BeginSearch += new BeginSearchEventHandler(BeginSearch);
            this.tabWorks.EndSearch += new EndSearchEventHandler(EndSearch);
        }

        /// <summary>
        /// Sets the response format and updates the application settings.
        /// </summary>
        /// <param name="responseFormat">The Decibel API response format.</param>
        private void SetResponseFormat(DecibelResponseFormat responseFormat)
        {
            mnuJsonResponse.IsChecked = false;
            mnuXmlResponse.IsChecked = false;
            if (responseFormat == DecibelResponseFormat.JSON)
                mnuJsonResponse.IsChecked = true;
            else
                mnuXmlResponse.IsChecked = true;

            Settings.ResponseFormat = responseFormat;
            Settings.WriteApplicationSettings();
            
            tabAlbums.SetResponseFormat(responseFormat);
            tabParticipants.SetResponseFormat(responseFormat);
            tabTracks.SetResponseFormat(responseFormat);
            tabWorks.SetResponseFormat(responseFormat);
        }

        /// <summary>
        /// Restores the size, state and location of the window.
        /// </summary>
        private void RestoreWindowState()
        {
            if (Properties.Settings.Default.WindowTop != 0 && Properties.Settings.Default.WindowLeft != 0)
            {
                this.Top = Properties.Settings.Default.WindowTop;
                this.Left = Properties.Settings.Default.WindowLeft;
            }
            else
                this.WindowStartupLocation = WindowStartupLocation.CenterScreen;

            this.Width = Properties.Settings.Default.WindowWidth;
            this.Height = Properties.Settings.Default.WindowHeight;

            this.WindowState = Properties.Settings.Default.WindowState;
        }

        /// <summary>
        /// Moves the window at least partially into view.
        /// </summary>
        public void MoveIntoView()
        {
            if (this.Top + this.Height > SystemParameters.VirtualScreenHeight)
                this.Top = SystemParameters.VirtualScreenHeight - this.Height - 40;

            if (this.Left + this.Width > SystemParameters.VirtualScreenWidth)
                this.Left = SystemParameters.VirtualScreenWidth - this.Width;

            if (this.Top < 0)
               this.Top = 0;

            if (this.Left < 0)
                this.Left = 0;
        }

        /// <summary>
        /// Sizes the window to fit the current screen.
        /// </summary>
        public void SizeToFit()
        {
            if (this.Height > SystemParameters.VirtualScreenHeight)
                this.Height = SystemParameters.VirtualScreenHeight;

            if (this.Width > SystemParameters.VirtualScreenWidth)
                this.Width = SystemParameters.VirtualScreenWidth;
        }
        
        #endregion Methods

        #region Events

        /// <summary>
        /// Occurs when a search begins.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void BeginSearch(object sender, BeginSearchEventArgs e)
        {
            pgbProgress.Visibility = Visibility.Visible;
            txtStatus.Text = "Searching...";
        }
        
        /// <summary>
        /// Occurs when a search is complete.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void EndSearch(object sender, EndSearchEventArgs e)
        {           
            pgbProgress.Visibility = Visibility.Hidden;
            txtStatus.Text = "Ready";
        }

        /// <summary>
        /// Occurs when the status bar text is updated.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void UpdateStatus(object sender, UpdateStatusEventArgs e)
        {
            txtStatus.Text = e.StatusText;
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuNew_Click(object sender, RoutedEventArgs e)
        {
            if (tbcSearchTabs.SelectedIndex == 0)
                tabAlbums.InitUserControl();
            else if (tbcSearchTabs.SelectedIndex == 1)
                tabParticipants.InitUserControl();
            else if (tbcSearchTabs.SelectedIndex == 2)
                tabTracks.InitUserControl();
            else if (tbcSearchTabs.SelectedIndex == 3)
                tabWorks.InitUserControl();
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param> 
        private void mnuSaveAs_Click(object sender, RoutedEventArgs e)
        {
            if (tbcSearchTabs.SelectedIndex == 0)
                Util.SaveAs(tabAlbums.GetResponse());
            else if (tbcSearchTabs.SelectedIndex == 1)
                Util.SaveAs(tabParticipants.GetResponse());
            else if (tbcSearchTabs.SelectedIndex == 2)
                Util.SaveAs(tabTracks.GetResponse());
            else if (tbcSearchTabs.SelectedIndex == 3)
                Util.SaveAs(tabWorks.GetResponse());
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuPrint_Click(object sender, RoutedEventArgs e)
        {
            if (tbcSearchTabs.SelectedIndex == 0)
                Util.PrintComponent(tabAlbums);
            else if (tbcSearchTabs.SelectedIndex == 1)
                Util.PrintComponent(tabParticipants);
            else if (tbcSearchTabs.SelectedIndex == 2)
                Util.PrintComponent(tabTracks);
            else if (tbcSearchTabs.SelectedIndex == 3)
                Util.PrintComponent(tabWorks);
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuExit_Click(object sender, RoutedEventArgs e)
        {
            Application.Current.Shutdown();
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuOptions_Click(object sender, RoutedEventArgs e)
        {
            OptionsWindow winOptions = new OptionsWindow();
            winOptions.ShowDialog();
            tabAlbums.RefreshSearchUrl();
            tabParticipants.RefreshSearchUrl();
            tabTracks.RefreshSearchUrl();
            tabWorks.RefreshSearchUrl();
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuJsonResponse_Click(object sender, RoutedEventArgs e)
        {
            SetResponseFormat(DecibelResponseFormat.JSON);
        }

        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuXmlResponse_Click(object sender, RoutedEventArgs e)
        {
            SetResponseFormat(DecibelResponseFormat.XML);
        }
        
        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuDocumentation_Click(object sender, RoutedEventArgs e)
        {
            Process.Start("https://developer.decibel.net/sample-code-rest-csharp");
        }
        
        /// <summary>
        /// Occurs when a menu item is clicked.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void mnuAbout_Click(object sender, RoutedEventArgs e)
        {
            MessageBox.Show("Decibel REST API Sample Project\nDecibel Music Systems", "About", MessageBoxButton.OK, MessageBoxImage.Information);
        }

        /// <summary>
        /// Occurs when the window has rendered or changed its rendering size.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void Window_SizeChanged(object sender, SizeChangedEventArgs e)
        {
            if (this.WindowState == WindowState.Normal)
            {
                Properties.Settings.Default.WindowWidth = this.Width;
                Properties.Settings.Default.WindowHeight = this.Height;
                Properties.Settings.Default.Save();
            }
        }

        /// <summary>
        /// Occurs when the window's location changes.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void Window_LocationChanged(object sender, EventArgs e)
        {
            if (this.WindowState == WindowState.Normal)
            {
                Properties.Settings.Default.WindowTop = this.Top;
                Properties.Settings.Default.WindowLeft = this.Left;
                Properties.Settings.Default.Save();
            }
        }

        /// <summary>
        /// Occurs directly after Close is called, and can be handled to cancel window closure.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void Window_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            if (this.WindowState == WindowState.Normal)
            {
                Properties.Settings.Default.WindowWidth = this.Width;
                Properties.Settings.Default.WindowHeight = this.Height;
                Properties.Settings.Default.WindowTop = this.Top;
                Properties.Settings.Default.WindowLeft = this.Left;
            }
            else
            {
                Properties.Settings.Default.WindowWidth = this.RestoreBounds.Width;
                Properties.Settings.Default.WindowHeight = this.RestoreBounds.Height;
                Properties.Settings.Default.WindowTop = this.RestoreBounds.Top;
                Properties.Settings.Default.WindowLeft = this.RestoreBounds.Left;
            }

            if (this.WindowState != WindowState.Minimized)
                Properties.Settings.Default.WindowState = this.WindowState;

            Properties.Settings.Default.Save();
        }
        
        #endregion Events
    }

    #region EventHandlers

    /// <summary>
    /// Custom event handler for BeginSearch event.
    /// </summary>
    /// <param name="sender">The object that raised the event.</param>
    /// <param name="e">Event arguments.</param>
    public delegate void BeginSearchEventHandler(object sender, BeginSearchEventArgs e);

    /// <summary>
    /// Custom event arguments for BeginSearch event.
    /// </summary>
    public class BeginSearchEventArgs : EventArgs
    {
        public Uri Uri;
    }
    
    /// <summary>
    /// Custom event handler for EndSearch event.
    /// </summary>
    /// <param name="sender">The object that raised the event.</param>
    /// <param name="e">Event arguments.</param>
    public delegate void EndSearchEventHandler(object sender, EndSearchEventArgs e);

    /// <summary>
    /// Custom event arguments for EndSearch event.
    /// </summary>
    public class EndSearchEventArgs : EventArgs
    {
        public int ResultCount;
    }
    
    /// <summary>
    /// Custom event handler for UpdateStatus event.
    /// </summary>
    /// <param name="sender">The object that raised the event.</param>
    /// <param name="e">Event arguments.</param>
    public delegate void UpdateStatusEventHandler(object sender, UpdateStatusEventArgs e);

    /// <summary>
    /// Custom event arguments for UpdateStatus event.
    /// </summary>
    public class UpdateStatusEventArgs : EventArgs
    {
        public string StatusText;
    }

    #endregion EventHandlers

}
