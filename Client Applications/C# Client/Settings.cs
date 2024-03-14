using System;
using System.Collections.Generic;
using System.Linq;
using System.Xml;
using System.Xml.Linq;

namespace DecibelRestWpf
{
    /// <summary>
    /// Reads and writes application settings.
    /// </summary>
    public class Settings
    {
        #region Fields

        /// <summary>
        /// The Decibel API Address.
        /// </summary>
        public static string ApiAddress { get; set; }

        /// <summary>
        /// The Decibel Application ID.
        /// </summary>
        public static string ApplicationID { get; set; }

        /// <summary>
        /// The Decibel Application Key.
        /// </summary>
        public static string ApplicationKey { get; set; }

        /// <summary>
        /// The Decibel API response format.
        /// </summary>
        public static DecibelResponseFormat ResponseFormat { get; set; }

        /// <summary>
        /// The default API Address.
        /// </summary>
        public static string DefaultApiAddress = "http://decibel-rest-jazz.cloudapp.net/v1/";

        #endregion Fields

        #region Methods

        /// <summary>
        /// Read the application settings.
        /// </summary>
        /// <returns>True if the settings have been read successfully; otherwise, false.</returns>
        public static bool ReadApplicationSettings()
        {
            try
            {
                ApiAddress = Properties.Settings.Default.ApiAddress;
                ApplicationID = Properties.Settings.Default.ApplicationID;
                ApplicationKey = Properties.Settings.Default.ApplicationKey;
                ResponseFormat = (DecibelResponseFormat)Properties.Settings.Default.ResponseFormat;
                return true;
            }
            catch
            {
                ApiAddress = DefaultApiAddress;
                ApplicationID = String.Empty;
                ApplicationKey = String.Empty;
                ResponseFormat = DecibelResponseFormat.JSON;
                return false;
            }
        }

        /// <summary>
        /// Writes the application settings.
        /// </summary>
        /// <returns>True if the settings have been written successfully; otherwise, false.</returns>
        public static bool WriteApplicationSettings()
        {
            try
            {
                Properties.Settings.Default.ApiAddress = ApiAddress;
                Properties.Settings.Default.ApplicationID = ApplicationID;
                Properties.Settings.Default.ApplicationKey = ApplicationKey;
                Properties.Settings.Default.ResponseFormat = (int)ResponseFormat;
                Properties.Settings.Default.Save();
                return true;
            }
            catch 
            { 
                return false; 
            }
        }

        #endregion Methods
    }

    #region Enumerations

    /// <summary>
    /// Enumeration of values that define the Decibel API response format.
    /// </summary>
    public enum DecibelResponseFormat
    {
        /// <summary>
        /// XML response format.
        /// </summary>
        XML = 1,
        /// <summary>
        /// JSON response format.
        /// </summary>
        JSON = 2
    }

    #endregion Enumerations

}
