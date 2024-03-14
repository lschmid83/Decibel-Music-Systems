package decibelrestjava;

/**
 * Constructs Decibel REST request URLs.
 */
public class DecibelUrlBuilder {

    /**
     * The Decibel API address.
     */
    private String mBaseUrl;

    /**
     * Creates new DecibelUrlBuilder. 
     *
     * @param url The Decibel API address.
     */
    public DecibelUrlBuilder(String url) {
        mBaseUrl = url;
    }

    /**
     * Gets the album search URL with the specified search parameters.
     *
     * @param searchParam The album search parameters.
     * @return The album search URL.
     */
    public String getAlbumSearchUrl(AlbumSearchParam searchParam) {
        String param = "";

        if (!Util.isNullOrWhiteSpace(searchParam.AlbumTitle)) {
            param += "albumTitle=" + searchParam.AlbumTitle + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Barcode)) {
            param += "barcode=" + searchParam.Barcode + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.ArtistName)) {
            param += "artist=" + searchParam.ArtistName + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Genre)) {
            param += "genre=" + searchParam.Genre + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Label)) {
            param += "label=" + searchParam.Label + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Depth)) {
            param += "depth=" + searchParam.Depth + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Format)) {
            param += "format=" + searchParam.Format + "&";
        }
        
        if (!Util.isNullOrWhiteSpace(searchParam.PageSize)) {
            param += "pageSize=" + searchParam.PageSize + "&";
        }     
        
        if (param.length() > 0) {
            param = param.substring(0, param.length() - 1);
        }
        return mBaseUrl + "Albums/?" + param.replace(" ", "%20");
    }

    /**
     * Gets the participant search URL with the specified search parameters.
     *
     * @param searchParam The participant search parameters.
     * @return The participant search URL.
     */
    public String getParticipantSearchUrl(ParticipantSearchParam searchParam) {
        String param = "";

        if (!Util.isNullOrWhiteSpace(searchParam.Name)) {
            param += "name=" + searchParam.Name + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Activity)) {
            param += "activity=" + searchParam.Activity + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.DateBorn)) {
            param += "dateBorn=" + searchParam.DateBorn + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.DateDied)) {
            param += "dateDied=" + searchParam.DateDied + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Depth)) {
            param += "depth=" + searchParam.Depth + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Format)) {
            param += "format=" + searchParam.Format + "&";
        }
        
        if (!Util.isNullOrWhiteSpace(searchParam.PageSize)) {
            param += "pageSize=" + searchParam.PageSize + "&";
        }     

        if (param.length() > 0) {
            param = param.substring(0, param.length() - 1);
        }
        return mBaseUrl + "Participants/?" + param.replace(" ", "%20");
    }

    /**
     * Gets the track search URL with the specified search parameters.
     *
     * @param searchParam The track search parameters.
     * @return The track search URL.
     */
    public String getTrackSearchUrl(TrackSearchParam searchParam) {

        String param = "";

        if (!Util.isNullOrWhiteSpace(searchParam.TrackTitle)) {
            param += "trackTitle=" + searchParam.TrackTitle + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Artist)) {
            param += "artist=" + searchParam.Artist + "&";
        }
        
        if (!Util.isNullOrWhiteSpace(searchParam.AlbumTitle)) {
            param += "albumTitle=" + searchParam.AlbumTitle + "&";
        }      

        if (!Util.isNullOrWhiteSpace(searchParam.Depth)) {
            param += "depth=" + searchParam.Depth + "&";
        }
        
        if (!Util.isNullOrWhiteSpace(searchParam.Format)) {
            param += "format=" + searchParam.Format + "&";
        }
        
        if (!Util.isNullOrWhiteSpace(searchParam.PageSize)) {
            param += "pageSize=" + searchParam.PageSize + "&";
        }     

        param = param.substring(0, param.length() - 1);
        return mBaseUrl + "Tracks/?" + param.replace(" ", "%20");
    }

    /**
     * Gets the work search URL with the specified search parameters.
     *
     * @param searchParam The work search parameters.
     * @return The work search URL.
     */
    public String getWorkSearchUrl(WorkSearchParam searchParam) {

        String param = "";

        if (!Util.isNullOrWhiteSpace(searchParam.Name)) {
            param += "name=" + searchParam.Name + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Composers)) {
            param += "composers=" + searchParam.Composers + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Catalogue)) {
            param += "catalogue=" + searchParam.Catalogue + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.PageSize)) {
            param += "pageSize=" + searchParam.PageSize + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Depth)) {
            param += "depth=" + searchParam.Depth + "&";
        }

        if (!Util.isNullOrWhiteSpace(searchParam.Format)) {
            param += "format=" + searchParam.Format + "&";
        }

        param = param.substring(0, param.length() - 1);
        return mBaseUrl + "Works/?" + param.replace(" ", "%20");
    }

    /**
     * Stores album search parameters.
     */
    public static class AlbumSearchParam {

        public String AlbumTitle;
        public String Barcode;
        public String ArtistName;
        public String Genre;
        public String Label;
        public String Depth;
        public String Format;
        public String PageSize;
    }

    /**
     * Stores participant search parameters.
     */
    public static class ParticipantSearchParam {

        public String Name;
        public String Activity;
        public String DateBorn;
        public String DateDied;
        public String Depth;
        public String Format;
        public String PageSize;
    }

    /**
     * Stores track search parameters.
     */
    public static class TrackSearchParam {

        public String TrackTitle;
        public String Artist;
        public String AlbumTitle;
        public String Depth;
        public String Format;
        public String PageSize;
    }

    /**
     * Stores work search parameters.
     */
    public static class WorkSearchParam {

        public String Name;
        public String Composers;
        public String Catalogue;
        public String Depth;
        public String Format;
        public String PageSize;
    }
}
