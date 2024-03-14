using System;

namespace DecibelRestWpf.Http
{
    /// <summary>
    /// Constructs Decibel REST request URLs.
    /// </summary>
    public class DecibelUrlBuilder
    {
        #region Fields

        /// <summary>
        /// The Decibel API address.
        /// </summary>
        private string mApiAddress;

        #endregion Fields

        #region Constructors

        /// <summary>
        /// Creates new DecibelUrlBuilder. 
        /// </summary>
        /// <param name="url">The Decibel API address.</param>
        public DecibelUrlBuilder(string url)
        {
            mApiAddress = url;
        }

        #endregion Constructors

        #region Methods

        /// <summary>
        /// Gets the album search URL with the specified search parameters.
        /// </summary>
        /// <param name="searchParam">The album search parameters.</param>
        /// <returns>The album search URL.</returns>
        public Uri GetAlbumSearchUrl(AlbumSearchParam searchParam)
        {
            string param = String.Empty;
            
            if (!String.IsNullOrWhiteSpace(searchParam.AlbumTitle))
                param += "albumTitle=" + searchParam.AlbumTitle + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Barcode))
                param += "barcode=" + searchParam.Barcode + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.ArtistName))
                param += "artist=" + searchParam.ArtistName + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Genre))
                param += "genre=" + searchParam.Genre + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Label))
                param += "label=" + searchParam.Label + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Depth))
                param += "depth=" + searchParam.Depth + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Format))
                param += "format=" + searchParam.Format + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.PageSize))
                param += "pageSize=" + searchParam.PageSize + "&";

            if(param.Length > 0)
                param = param.Substring(0, param.Length - 1);
            param = param.Replace("\"", "");

            Uri uri;
            try
            {
                uri = new Uri(mApiAddress + "Albums/?" + param);
            }
            catch 
            {
                uri = new Uri(Settings.DefaultApiAddress + "Albums/?" + param);
            }
            return uri;
        }

        /// <summary>
        /// Gets the participant search URL with the specified search parameters.
        /// </summary>
        /// <param name="searchParam">The participant search parameters.</param>
        /// <returns>The participant search URL.</returns>
        public Uri GetParticipantSearchUrl(ParticipantSearchParam searchParam)
        {
            string param = String.Empty;

            if (!String.IsNullOrWhiteSpace(searchParam.Name))
                param += "name=" + searchParam.Name + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Activity))
                param += "activity=" + searchParam.Activity + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.DateBorn))
                param += "dateBorn=" + searchParam.DateBorn.Replace("/", String.Empty) + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.DateDied))
                param += "dateDied=" + searchParam.DateDied.Replace("/", String.Empty) + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Depth))
                param += "depth=" + searchParam.Depth + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Format))
                param += "format=" + searchParam.Format + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.PageSize))
                param += "pageSize=" + searchParam.PageSize + "&";

            if (param.Length > 0)
                param = param.Substring(0, param.Length - 1);
            param = param.Replace("\"", "");

            Uri uri;
            try
            {
                uri = new Uri(mApiAddress + "Participants/?" + param);
            }
            catch
            {
                uri = new Uri(Settings.DefaultApiAddress + "Participants/?" + param);   
            } 
            return uri;
       }

        /// <summary>
        /// Gets the track search URL with the specified search parameters.
        /// </summary>
        /// <param name="searchParam">The track search parameters.</param>
        /// <returns>The track search URL.</returns>
        public Uri GetTrackSearchUrl(TrackSearchParam searchParam)
        {
            string param = String.Empty;

            if (!String.IsNullOrWhiteSpace(searchParam.TrackTitle))
                param += "trackTitle=" + searchParam.TrackTitle + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Artist))
                param += "artist=" + searchParam.Artist + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.AlbumTitle))
                param += "albumTitle=" + searchParam.AlbumTitle + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Depth))
                param += "depth=" + searchParam.Depth + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Format))
                param += "format=" + searchParam.Format + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.PageSize))
                param += "pageSize=" + searchParam.PageSize + "&";

            if (param.Length > 0)
                param = param.Substring(0, param.Length - 1);
            param = param.Replace("\"", "");

            Uri uri;
            try
            {
                uri = new Uri(mApiAddress + "Tracks/?" + param);
            }
            catch
            {
                uri = new Uri(Settings.DefaultApiAddress + "Participants/?" + param);
            }
            return uri;
        }

        /// <summary>
        /// Gets the work search URL with the specified search parameters.
        /// </summary>
        /// <param name="searchParam">The work search parameters.</param>
        /// <returns>The work search URL.</returns>
        public Uri GetWorkSearchUrl(WorkSearchParam searchParam)
        {
            string param = String.Empty;

            if (!String.IsNullOrWhiteSpace(searchParam.Name))
                param += "name=" + searchParam.Name + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Composers))
                param += "composers=" + searchParam.Composers + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Catalogue))
                param += "catalogue=" + searchParam.Catalogue + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Depth))
                param += "depth=" + searchParam.Depth + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.Format))
                param += "format=" + searchParam.Format + "&";

            if (!String.IsNullOrWhiteSpace(searchParam.PageSize))
                param += "pageSize=" + searchParam.PageSize + "&";

            if (param.Length > 0)
                param = param.Substring(0, param.Length - 1);
            param = param.Replace("\"", "");

            Uri uri;
            try
            {
                uri = new Uri(mApiAddress + "Works/?" + param);
            }
            catch 
            {
                uri = new Uri(Settings.DefaultApiAddress + "Works/?" + param);
            }
            return uri;
        }

        #endregion Methods
    }

    #region SearchParameters

    /// <summary>
    /// Stores album search parameters.
    /// </summary>
    public class AlbumSearchParam
    {
        public string AlbumTitle { get; set; }
        public string Barcode { get; set; }
        public string ArtistName { get; set; }
        public string Genre { get; set; }
        public string Label { get; set; }
        public string Depth { get; set; }
        public string Format { get; set; }
        public string PageSize { get; set; }
    }

    /// <summary>
    /// Stores participant search parameters.
    /// </summary>
    public class ParticipantSearchParam
    {
        public string Name { get; set; }
        public string Activity { get; set; }
        public string DateBorn { get; set; }
        public string DateDied { get; set; }
        public string Depth { get; set; }
        public string Format { get; set; }
        public string PageSize { get; set; }
    }

    /// <summary>
    /// Stores track search parameters.
    /// </summary>
    public class TrackSearchParam
    {
        public string TrackTitle { get; set; }
        public string Artist { get; set; }
        public string AlbumTitle { get; set; }
        public string Depth { get; set; }
        public string Format { get; set; }
        public string PageSize { get; set; }
    }

    /// <summary>
    /// Stores work search parameters.
    /// </summary>
    public class WorkSearchParam
    {
        public string Name { get; set; }
        public string Composers { get; set; }
        public string Catalogue { get; set; }
        public string Depth { get; set; }
        public string Format { get; set; }
        public string PageSize { get; set; }
    }

    #endregion SearchParameters
}
