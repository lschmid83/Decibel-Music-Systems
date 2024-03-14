using System;
using System.Windows;
using System.Windows.Controls;

namespace DecibelRestWpf.UserControls
{
    /// <summary>
    /// Custom user control for navigating paged result sets.
    /// </summary>
    public partial class PageNavigator : UserControl
    {
        #region Fields

        /// <summary>
        /// The current page number.
        /// </summary>
        private int mCurrentPage;

        /// <summary>
        /// The total number of pages.
        /// </summary>
        private int mNumberOfPages;

        #endregion Fields

        #region EventHandlers

        /// <summary>
        /// Occurs when a page is selected.
        /// </summary>
        public event PageChangedEventHandler PageChanged;

        /// <summary>
        /// Custom event handler for the PageChanged event.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        public delegate void PageChangedEventHandler(object sender, PageChangedEventArgs e);

        /// <summary>
        /// Custom event arguments for PageChanged event.
        /// </summary>
        public class PageChangedEventArgs : EventArgs
        {
            public int PageNumber;
        }

        #endregion EventHandlers

        #region Constructors

        /// <summary>
        /// Creates new PageNavigator UserControl.
        /// </summary>
        public PageNavigator()
        {
            InitializeComponent();
            btnNext.IsEnabled = false;
            btnBack.IsEnabled = false;
            cmbPageNum.IsEnabled = false;
        }

        #endregion Constructors

        #region Methods

        /// <summary>
        /// Sets the total number of pages.
        /// </summary>
        /// <param name="numberOfPages">The total number of pages.</param>
        private void SetNumberOfPages(int numberOfPages)
        {
            if (numberOfPages <= 0)
            {
                this.mNumberOfPages = 0;
                cmbPageNum.IsEnabled = false;
                btnBack.IsEnabled = false;
                btnNext.IsEnabled = false;
            }
            else
            {
                cmbPageNum.IsEnabled = true;
                if (this.CurrentPage > 1)
                    btnBack.IsEnabled = true;
                else
                    btnBack.IsEnabled = false;

                if (this.CurrentPage < numberOfPages)
                    btnNext.IsEnabled = true;
                else
                    btnNext.IsEnabled = false;
            }
            cmbPageNum.Items.Clear();
            for (int i = 0; i < numberOfPages; i++)
            {
                cmbPageNum.Items.Add((i + 1).ToString());
            }
            txtNumPages.Text = "Pages " + numberOfPages;
            mNumberOfPages = numberOfPages;

            SetCurrentPage(CurrentPage, false);
        }

        /// <summary>
        /// Gets the current page number.
        /// </summary>
        /// <returns>The current page number.</returns>
        private int GetCurrentPage()
        {
            if (mNumberOfPages == 0)
            {
                return 0;
            }
            else return mCurrentPage;
        }

        /// <summary>
        /// Sets the current page number.
        /// </summary>
        /// <param name="value">The page number.</param>
        /// <param name="triggerEvent">Is the PageChanged event raised.</param>
        public void SetCurrentPage(int value, bool triggerEvent)
        {
            if (mNumberOfPages == 0)
            {
                btnBack.IsEnabled = false;
                btnNext.IsEnabled = false;
                mCurrentPage = 0;
            }
            else if (value <= 0)
            {
                btnBack.IsEnabled = false;
                btnNext.IsEnabled = false;
                mCurrentPage = 0;
            }
            else if (value >= mNumberOfPages)
            {
                mCurrentPage = mNumberOfPages;
                btnNext.IsEnabled = false;
                if (mCurrentPage > 1)
                    btnBack.IsEnabled = true;
                else
                    btnBack.IsEnabled = false;
            }
            else
            {
                mCurrentPage = value;
                if (mCurrentPage > 1)
                    btnBack.IsEnabled = true;
                else
                    btnBack.IsEnabled = false;
                if (mCurrentPage < mNumberOfPages)
                    btnNext.IsEnabled = true;
                else
                    btnNext.IsEnabled = false;
            }

            cmbPageNum.SelectionChanged -= new SelectionChangedEventHandler(cmbPageNum_SelectionChanged);
            cmbPageNum.SelectedIndex = mCurrentPage - 1;
            cmbPageNum.SelectionChanged += new SelectionChangedEventHandler(cmbPageNum_SelectionChanged);
            if (triggerEvent)
                this.PageChanged(this, new PageChangedEventArgs() { PageNumber = CurrentPage });
        }

        #endregion Methods

        #region Properties

        /// <summary>
        /// Gets or sets the number of pages.
        /// </summary>
        public int NumberOfPages
        {
            get { return mNumberOfPages; }
            set { SetNumberOfPages(value); }
        }
        
        /// <summary>
        /// Gets or sets the current page number.
        /// </summary>
        public int CurrentPage
        {
            get { return GetCurrentPage(); }
            set { SetCurrentPage(value, true); }
        }

        #endregion Properties

        #region Events

        /// <summary>
        /// Occurs when the back button is clicked. 
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnBack_Click(object sender, RoutedEventArgs e)
        {
            CurrentPage--;
        }

        /// <summary>
        /// Occurs when the next button is clicked. 
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void btnNext_Click(object sender, RoutedEventArgs e)
        {
            CurrentPage++;
        }

        /// <summary>
        /// Occurs when a page is selected from the combo box.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        private void cmbPageNum_SelectionChanged(object sender, SelectionChangedEventArgs e)
        {
            if (cmbPageNum.SelectedItem != null)
            {
                int pageNum = Convert.ToInt32(cmbPageNum.SelectedItem);
                SetCurrentPage(pageNum, true);
            }
        }

        #endregion Events
    }
}
