/**
 * Initializes the jQuery UI components.
 */
function initAlbumInfo()
{
	// Declare global variables to store the album and track listing
	window.album = new Object();
	window.trackList = new Object();

	$(".tabAlbumsTrackList").flexigrid({
		url : '',
		dataType : 'json',
		colModel : [ {
			display : 'ID',
			name : 'id',
			width : 40,
			sortable : true,
			align : 'left',
			hide: true
		}, {
			display : 'Index',
			name : 'index',
			width : 40,
			sortable : true,
			align : 'left',
			hide: true
		}, {
			display : '#',
			name : 'trackNumber',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Title',
			name : 'title',
			width : 315,
			sortable : true,
			align : 'left'
		}, {
			display : 'Length',
			name : 'length',
			width : 100,
			sortable : true,
			align : 'left'
		} ],
		sortname : "iso",
		sortorder : "asc",
		showToggleBtn : false,
		usepager : false,
		useRp : true,
		nohresize : true,
		resizable: false, 
		onDragCol: false,
		rp : 15,
		showTableToggleBtn : true,
		method : "GET",
		width : $("tabAlbumsTrackList").innerWidth(),
		height : 170,
		singleSelect: true,
		 onSuccess: function(){
			$('.tabAlbumsTrackList tr').dblclick(function(event){		
				var index = $('td[abbr="index"] >div', this).html();
				setFlexigridRow(index, '.tabAlbumsTrackList');
				showTrackParticipantsDialog(window.trackList["tabAlbums"][index]);
			});
		 } 
	})

	$(".tabTracksTrackList").flexigrid({
		url : '',
		dataType : 'json',
		colModel : [ {
			display : 'ID',
			name : 'id',
			width : 40,
			sortable : true,
			align : 'left',
			hide: true
		}, {
			display : 'Index',
			name : 'index',
			width : 40,
			sortable : true,
			align : 'left',
			hide: true
		}, {
			display : '#',
			name : 'trackNumber',
			width : 40,
			sortable : true,
			align : 'left'
		}, {
			display : 'Title',
			name : 'title',
			width : 315,
			sortable : true,
			align : 'left'
		}, {
			display : 'Length',
			name : 'length',
			width : 100,
			sortable : true,
			align : 'left'
		} ],
		sortname : "iso",
		sortorder : "asc",
		showToggleBtn : false,
		usepager : false,
		useRp : true,
		nohresize : true,
		resizable: false, 
		onDragCol: false,
		rp : 15,
		showTableToggleBtn : true,
		method : "GET",
		width : $("tabTracksTrackList").innerWidth(),
		height : 170,
		singleSelect: true,
		 onSuccess: function(){
			$('.tabTracksTrackList tr').dblclick(function(event){		
				var index = $('td[abbr="index"] >div', this).html();
				setFlexigridRow(index, '.tabTracksTrackList');
				showTrackParticipantsDialog(window.trackList["tabTracks"][index]);
			});
		 } 
	})

	$(".trackParticipants").flexigrid({
		url : '',
		dataType : 'json',
		colModel : [ {
			display : 'ID',
			name : 'id',
			width : 40,
			sortable : false,
			align : 'left',
			hide: true
		}, {
			display : 'Index',
			name : 'index',
			width : 40,
			sortable : false,
			align : 'left'
		}, {
			display : 'Name',
			name : 'name',
			width : 295,
			sortable : false,
			align : 'left'
		}, {
			display : 'Involvement',
			name : 'artists',
			width : 295,
			sortable : false,
			align : 'left'
		} ],
		sortname : "iso",
		sortorder : "asc",
		showToggleBtn : false,
		usepager : false,
		useRp : true,
		nohresize : true,
		resizable: false, 
		onDragCol: false,
		rp : 15,
		showTableToggleBtn : true,
		method : "GET",
		width : $("trackParticipants").innerWidth(),
		height : 220,
		singleSelect: true,
	})			
}

/**
 * Makes an AJAX call to request album information and displays the results.
 * @param track The Decibel album ID.
 * @param tabPage The tab page containing the album-info.html layout. 
 */		
function refreshAlbumInformation(albumID, tabPage)
{
	clearAlbumInformation(tabPage);
	
	// Make AJAX call to update album information
	window.albumClient;
	setStatus(tabPage + "Footer", "Searching...", true);
	if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
		  window.albumClient = new XMLHttpRequest();
	}
	else { // code for IE6, IE5
	  window.albumClient = new ActiveXObject("Microsoft.XMLHTTP");
	}
	window.albumClient.abort(); // Abort any pending requests
	window.albumClient.onreadystatechange = function() {
		if (window.albumClient.readyState == 4 && window.albumClient.status == 200) {
	
			try	{
				// Parse the JSON response into an object
				var obj = jQuery.parseJSON(window.albumClient.responseText);
			}
			catch(err) {
				setStatus(tabPage + "Footer", "Ready", false);
				return;
			}
			
			// Store the album in a global variable
			a = obj.ResultSet[0];
			window.album[tabPage] = a;

			// Store the AlbumInformation elements
			var inputElements = document.getElementById(tabPage + "AlbumInformation").getElementsByTagName("input");
			var selectElements = document.getElementById(tabPage + "AlbumInformation").getElementsByTagName("select");
			
			// Album Title
			inputElements[0].value = a.Name;
			
			// Artists
			inputElements[1].value = a.Artists; 		
			
			// Discs
			selectElements[0].options.length = 0;
			var opt = selectElements[0].options;
			for (var i = 1; i <= a.DiscCount; i++) { 
				opt[opt.length] = new Option(i,i);
			}		
		
			// Number of tracks
			inputElements[2].value = a.TrackCount;	
		
			// Duration
			inputElements[3].value = formatTime(a.TotalSeconds);	

			// Format
			inputElements[4].value = a.Discs[0].MusicMedium.Name;		
			
			// Label
			if(a.Publications.length > 0)
				inputElements[5].value = a.Publications[0].Publisher.Name;			
			
			// Release Date
			inputElements[6].value = a.ReleaseDate.Name;	
		
			// Region
			inputElements[7].value = a.GeoEntities[0].GeoEntity.Name;	
			
			// Catalogue Number
			if(a.Publications.length > 0)
				inputElements[8].value = a.Publications[0].CatalogueNumber;		
			
			// Barcode
			var externalIdentifiers = a.ExternalIdentifiers;
			var barcode = "";
			for (var i = 0; i < externalIdentifiers.length; i++) {
				if(externalIdentifiers[i].ExternalDatabase.Name == 'UPC Barcode')
					barcode = externalIdentifiers[i].Identifier;
			}	
			inputElements[9].value = barcode;		
		
			// Genres
			var genreValues = a.Genres;
			var genres = "";
			for (var i = 0; i < genreValues.length; i++) {
				genres += genreValues[i].Genre.Name + ', ';
			}
			if(genres.length > 2)
				inputElements[10].value = genres.substring(0, genres.length - 2);			
	
			// Thumbnail
			var infoThumbnail = document.getElementById(tabPage).getElementsByTagName("img")[0];
			var infoThumbnailLink = document.getElementById(tabPage).getElementsByTagName("a")[0];
			
			if(a.Thumbnail.length > 0) {
			
				// Make AJAX call to request album thumbnail
				window.thumbnailClient;
				if (XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
					  window.thumbnailClient = new XMLHttpRequest();
				}
				else { // code for IE6, IE5
				  window.thumbnailClient = new ActiveXObject("Microsoft.XMLHTTP");
				}
				window.thumbnailClient.abort(); // Abort any pending requests
				window.thumbnailClient.onreadystatechange = function() {
					if (window.thumbnailClient.readyState == 4 && window.thumbnailClient.status == 200){ 
						infoThumbnail.src = window.thumbnailClient.responseText; // Set the thumbnail src tag to the base64 encoded string
						infoThumbnailLink.href = "javascript:showCoverArtDialog(a.ID);"; // Set the thumbnail link to open cover art dialog window
						infoThumbnail.style.display = 'inline';
						infoThumbnail.style.marginTop = '2px';
						infoThumbnail.style.marginLeft = '9px';
						setStatus(tabPage + "Footer", "Ready", false);
					}
				}
				window.thumbnailClient.open("GET","album-image.php?albumID=" + a.ID + "&type=Thumbnail", true); // Request the base64 encoded thumbnail image
				window.thumbnailClient.send();	
			}
			else
			{
				infoThumbnail.style.display = 'none'; // Hide the thumbnail image
				setStatus(tabPage + "Footer", "Ready", false);
			}
			
			// Set the selected disc and update the track list
			discChanged(selectElements[0].value, tabPage);
		}
	}
	// Request album by ID
	window.albumClient.open("GET","request.php?search=AlbumInformation&id=" + albumID, true); 
	window.albumClient.send();	
}

/**
 * Refreshes the track list.
 * @param track The disc number.
 * @param tabPage The tab page containing the album-info.html layout. 
 */	
function discChanged(discNumber, tabPage) 
{
	tracks = window.album[tabPage].Tracks;
	flexigridID = "." + tabPage + "TrackList";
		
	// Store the tracks for the disc number
	window.trackList[tabPage] = []
	for (var i = 0; i < tracks.length; i++) {
		if (tracks[i].DiscNumber == discNumber) {
			window.trackList[tabPage].push(tracks[i]); 
		}
	}
	
	// Update the track list Flexigrid 
	$(flexigridID).flexAddData(formatTracks(eval(window.trackList[tabPage])));		
	
	// Select table row
	setFlexigridRow(0, flexigridID); 
}	

/**
 * Converts a Decibel tracks array into Flexigrid JSON format.
 * @param tracks Array of tracks.
 * @return The Flexigrid JSON object.
 */
function formatTracks(tracks){
	
	// Create a JSON representation of tracks
	var rows = Array();
	count = 1;
	for (var i = 0; i < tracks.length; i++) {
		var item = tracks[i];
		rows.push({ cell: [item.ID,
						   i,
						   item.SequenceNo,
						   item.Name,
						   formatTime(item.TotalSeconds) ]
		});	
		count++;
	}	

	// Flexigrid requires a format as follows:
	return {
		 total: tracks.length,
		 page: 1,
		 rows: rows
	};
}

/**
 * Displays the cover art in a jQuery dialog window.
 * @param albumID The Decibel album ID.
 */	
function showCoverArtDialog(albumID)
{
	document.getElementById("coverArtImage").style.visibility = "hidden";

	$( "#coverArtDialog" ).dialog({
		width: 480,
		height: 450,
		resizable: false,
		title: "Cover Art",
		modal: true
	});	
	
	// Make AJAX call to request album cover art
	var xmlhttp;
	if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
		  xmlhttp=new XMLHttpRequest();
	}
	else { // code for IE6, IE5
	  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	}
	xmlhttp.onreadystatechange=function() {
		if (xmlhttp.readyState==4 && xmlhttp.status==200) {
			document.getElementById("coverArtImage").src = xmlhttp.responseText;
			document.getElementById("coverArtImage").style.visibility = "visible";
		}
	}
	xmlhttp.open("GET","album-image.php?albumID=" + albumID + "&type=Image", true);
	xmlhttp.send();		
}	

/**
 * Displays the track participations in a jQuery dialog window.
 * @param track The Decibel Track object.
 */
function showTrackParticipantsDialog(track)
{
	participations = track.Performances[0].Participations;
	$(".trackParticipants").flexAddData(formatTrackParticipations(eval(participations)));		
	setFlexigridRow(0, ".trackParticipants");
	
	$("#okButton").button().click(function( event ) {				
		$("#trackParticipantsDialog").dialog("close");
	});		
		
	$("#okButton").attr("tabindex","-1");		

	$( "#trackParticipantsDialog" ).dialog({
		width: 715,
		height: 325,
		resizable: false,
		title: "Track Participants",
		modal: true
	});
}	

/**
 * Converts a Decibel track participations array into Flexigrid JSON format.
 * @param participations Array of track participations.
 * @return The Flexigrid JSON object.
 */
function formatTrackParticipations(participations){
	
	// Create a JSON representation of track participations
	var rows = Array();
	count = 1;
	for (var i = 0; i < participations.length; i++) 
	{
		var item = participations[i];
		rows.push({ cell: [item.ID,
						   count,
						   item.Name.split(',')[0],
						   item.Name.split(',')[1] ]
		});	
		count++;
	}	

	// Flexigrid requires a format as follows:
	return {
		 total: participations.length,
		 page: 1,
		 rows: rows
	};
 }
 
/**
 * Clears the album information.
 * @param tabPage The tab page containing the album-info.html layout. 
 */		
function clearAlbumInformation(tabPage)
{
	// Get the elements contained in the selected tabPage
	var inputElements = document.getElementById(tabPage + "AlbumInformation").getElementsByTagName("input");
	var selectElements = document.getElementById(tabPage + "AlbumInformation").getElementsByTagName("select");	
	// Album Title
	inputElements[0].value = "";
	// Artists
	inputElements[1].value = ""; 		
	// Discs
	selectElements[0].options.length = 0;
	// Number of tracks
	inputElements[2].value = "";	
	// Duration
	inputElements[3].value = "";	
	// Format
	inputElements[4].value = "";			
	// Label
	inputElements[5].value = "";			
	// Release Date
	inputElements[6].value = "";	
	// Region
	inputElements[7].value = "";	
	// Catalogue Number
	inputElements[8].value = "";			
	// Barcode
	inputElements[9].value = "";		
	// Genres
	inputElements[10].value = "";			
	// Thumbnail
	var infoThumbnail = document.getElementById(tabPage).getElementsByTagName("img")[0];
	infoThumbnail.style.display = 'none';
	// Track List
	$("." + tabPage + "TrackList").flexAddData(clearFlexigrid());			
}