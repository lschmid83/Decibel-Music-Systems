using System;

namespace DecibelRestWpf.UserControls
{
    /// <summary>
    /// Interface for the search UserControls.
    /// </summary>
    interface ISearchUserControl : IDisposable 
    {
        /// <summary>
        /// Initializes the UserControl.
        /// </summary>
        void InitUserControl();

        /// <summary>
        /// Initializes the AutoCompleteBox controls.
        /// </summary>
        void InitAutoComplete();
               
        /// <summary>
        /// Begins the search.
        /// </summary>
        /// <param name="url">The REST request URL.</param>
        void Search(Uri url);

        /// <summary>
        /// Occurs when a page is selected from the result set.
        /// </summary>
        /// <param name="sender">The object that raised the event.</param>
        /// <param name="e">Event arguments.</param>
        void PageChanged(object sender, PageNavigator.PageChangedEventArgs e);
        
        /// <summary>
        /// Refreshes the search URL.
        /// </summary>
        void RefreshSearchUrl();

        /// <summary>
        /// Sets the response format.
        /// </summary>
        /// <param name="responseFormat">The Decibel API response format.</param>
        void SetResponseFormat(DecibelResponseFormat responseFormat);

        /// <summary>
        /// Gets the response string.
        /// </summary>
        /// <returns>The XML or JSON response string.</returns>
        string GetResponse();

        /// <summary>
        /// Makes a thread-safe call to update the Text property of the response TextBox.
        /// </summary>
        /// <param name="response">The XML or JSON response string.</param>
        void SetResponse(string response);
        
        /// <summary>
        /// Sends a request for autocomplete suggestions.
        /// </summary>
        /// <param name="text">The search text.</param>
        /// <param name="dictionary">The autocomplete dictionary.</param>
        void SendAutoCompleteRequest(string text, string dictionary);
    }
}
