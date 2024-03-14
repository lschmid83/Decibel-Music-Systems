using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Printing;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Xml;
using System.Xml.Serialization;
using JsonFormatter;
using Microsoft.Win32;

namespace DecibelRestWpf
{
    /// <summary>
    /// Contains utility functions.
    /// </summary>
    public class Util
    {
        #region Methods

        /// <summary>
        /// Deserializes an XML string into an object of type T.
        /// </summary>
        /// <typeparam name="T">The type of object.</typeparam>
        /// <param name="str">The XML string.</param>
        /// <returns>The object representation of the XML.</returns>
        public static T DeserializeXmlString<T>(string str)
        {
            if (str == null)
                return default(T);

            XmlSerializer serializer = new System.Xml.Serialization.XmlSerializer(typeof(T));

            using (TextReader reader = new StringReader(str))
            {
                return (T)(serializer.Deserialize(reader));
            }
        }

        /// <summary>
        /// Sets the WebClient request headers.
        /// </summary>
        /// <param name="client">The WebClient object.</param>
        public static void SetDecibelRequestHeaders(WebClient client)
        {
            client.Proxy = null;
            client.Encoding = Encoding.UTF8;
            client.Headers.Add("DecibelAppID", Settings.ApplicationID);
            client.Headers.Add("DecibelAppKey", Settings.ApplicationKey);
            client.Headers.Add("DecibelTimestamp", DateTime.Now.ToString("yyyyMMdd HH:mm:ss", System.Globalization.CultureInfo.InvariantCulture));
        }

        /// <summary>
        /// Checks for authentication errors.
        /// </summary>
        /// <param name="exception">Represents errors that occur during application execution.</param>
        public static void CheckAuthentication(Exception exception)
        {
            if (exception == null)
                return;
            WebException we = (WebException)exception;
            HttpWebResponse response = (HttpWebResponse)we.Response;
            if (response != null && response.StatusCode == HttpStatusCode.Forbidden)
                MessageBox.Show("Authentication Failed." + Environment.NewLine + "Please check your application ID and key are entered correctly." + Environment.NewLine, "Authentication Failed", MessageBoxButton.OK, MessageBoxImage.Error);
        }  

        /// <summary>
        /// Gets the album publishers from publication information.
        /// </summary>
        /// <param name="album">The Album object.</param>
        /// <returns>A list of publisher names.</returns>
        public static List<string> GetAlbumPublishers(Album album)
        {
            List<string> publishers = new List<string>();
            if (album.Publications != null)
            {
                foreach (Publication publication in album.Publications)
                    publishers.Add(publication.Publisher.Name);
            }
            return publishers;
        }

        /// <summary>
        /// Gets the album barcodes from the external identifier information.
        /// </summary>
        /// <param name="album">The Album object.</param>
        /// <returns>A list of barcodes.</returns>
        public static List<string> GetAlbumBarcodes(Album album)
        {
            List<string> barcodes = new List<string>();
            if(album.ExternalIdentifiers != null)
            {
                foreach (ExternalIdentifier externalIdentifier in album.ExternalIdentifiers)
                {
                    if (externalIdentifier.ExternalDatabase.Equals("UPC Barcode"))
                        barcodes.Add(externalIdentifier.Identifier);
                }
            }
            return barcodes;
        }

        /// <summary>
        /// Returns the first two retrieval depths from a string.
        /// </summary>
        /// <param name="retrievalDepth">The string containing retrieval depths.</param>
        /// <returns>The truncated retrieval depth string.</returns>
        public static string GetSearchDepthTruncated(string retrievalDepth)
        {
            string[] depth = retrievalDepth.Split(';');
            if (depth.Length >= 4)
                return depth[0] + ";" + depth[1] + "...";
            else
                return retrievalDepth;
        }

        /// <summary>
        /// Converts seconds to hh:mm:ss.
        /// </summary>
        /// <param name="seconds">Total number of seconds.</param>
        /// <returns>Seconds converted to a string in the format hh:mm:ss.</returns>
        public static string FormatTime(int seconds)
        {
            TimeSpan timeSpan = new TimeSpan(0, 0, Convert.ToInt32(seconds));
            return timeSpan.ToString("c");
        }

        /// <summary>
        /// Formats an XML string.
        /// </summary>
        /// <param name="xml">The XML string.</param>
        /// <returns>A formatted XML string.</returns>
        public static string FormatXml(string xml)
        {
            var doc = new XmlDocument();
            doc.LoadXml(xml);
            var stringBuilder = new StringBuilder();
            var xmlWriterSettings = new XmlWriterSettings { Indent = true, OmitXmlDeclaration = true };
            doc.Save(XmlWriter.Create(stringBuilder, xmlWriterSettings));
            return stringBuilder.ToString();
        }

        /// <summary>
        /// Formats a JSON String.
        /// </summary>
        /// <param name="json">The XML string.</param>
        /// <returns>A formatted XML string.</returns>
        public static string FormatJson(string json)
        {
            JsonFormat jf = new JsonFormat();
            return jf.PrettyPrint(json);
        }

        /// <summary>
        /// Gets a Bitmap image from a byte array.
        /// </summary>
        /// <param name="byteArray">The image byte array.</param>
        /// <returns>A Bitmap image.</returns>
        public static ImageSource GetImageSourceFromByteArray(byte[] byteArray)
        {
            if (byteArray == null)
                return null;
            else
            {
                try
                {
                    MemoryStream stream = new MemoryStream(byteArray);
                    BitmapImage image = new BitmapImage();
                    image.BeginInit();
                    image.StreamSource = stream;
                    image.EndInit();
                    return image;
                }
                catch
                {
                    return null;
                }
            }
        }

        /// <summary>
        /// Displays a save file dialog and saves a string to a file.
        /// </summary>
        /// <param name="text">The string to write to file.</param>
        public static void SaveAs(string text)
        {
            SaveFileDialog saveFileDialog = new SaveFileDialog();
            saveFileDialog.Filter = Settings.ResponseFormat + "|*." + Settings.ResponseFormat.ToString().ToLower();
            saveFileDialog.Title = "Save response";
            saveFileDialog.ShowDialog();

            if (!String.IsNullOrWhiteSpace(saveFileDialog.FileName))
            {
                using (TextWriter tw = new StreamWriter(saveFileDialog.OpenFile()))
                {
                    tw.WriteLine(text);
                }
            }
        }

        /// <summary>
        /// Sets the selected item in a DataGrid.
        /// </summary>
        /// <param name="dataGrid">The DataGrid object.</param>
        /// <param name="col">The selected column.</param>
        /// <param name="row">The selected row.</param>
        public static void SetDatagridItem(DataGrid dataGrid, int col, int row)
        {
            dataGrid.SelectedIndex = row;
            dataGrid.Focus();
            if (dataGrid.SelectedCells.Count == 0)
            {
                dataGrid.CurrentCell = new DataGridCellInfo(dataGrid.Items[dataGrid.SelectedIndex], dataGrid.Columns[col]);
                dataGrid.SelectedCells.Add(dataGrid.CurrentCell);
            }
            else
                dataGrid.CurrentCell = dataGrid.SelectedCells[col];
        }

        /// <summary>
        /// Initializes the AutoCompleteBox control properties.
        /// </summary>
        /// <param name="autocomplete">The AutoCompleteBox control.</param>
        public static void InitAutocompleteProperties(AutoCompleteBox autocomplete)
        {
            autocomplete.FilterMode = AutoCompleteFilterMode.Contains;
            autocomplete.MinimumPopulateDelay = 200;
            autocomplete.Text = String.Empty;
        }

        /// <summary>
        /// Prints the contents of a component scaled to fit the page.
        /// </summary>
        /// <param name="userControl">The UserControl to print.</param>
        public static void PrintComponent(UserControl userControl)
        {
            PrintDialog print = new PrintDialog();
            if (print.ShowDialog() == true)
            {
                PrintCapabilities capabilities = print.PrintQueue.GetPrintCapabilities(print.PrintTicket);

                if (capabilities.PageImageableArea != null)
                {
                    double scale = Math.Min(capabilities.PageImageableArea.ExtentWidth / userControl.ActualWidth,
                                            capabilities.PageImageableArea.ExtentHeight / userControl.ActualHeight);

                    Transform oldTransform = userControl.LayoutTransform;

                    userControl.LayoutTransform = new ScaleTransform(scale, scale);

                    Size oldSize = new Size(userControl.ActualWidth, userControl.ActualHeight);
                    Size sz = new Size(capabilities.PageImageableArea.ExtentWidth, capabilities.PageImageableArea.ExtentHeight);
                    userControl.Measure(sz);
                    ((UIElement)userControl).Arrange(new Rect(new System.Windows.Point(capabilities.PageImageableArea.OriginWidth, capabilities.PageImageableArea.OriginHeight), sz));

                    print.PrintVisual(userControl, "Print Results");
                    userControl.LayoutTransform = oldTransform;
                    userControl.Measure(oldSize);

                    ((UIElement)userControl).Arrange(new Rect(new System.Windows.Point(0, 0), oldSize));
                }
            }
        }

        #endregion Methods
    }  
}