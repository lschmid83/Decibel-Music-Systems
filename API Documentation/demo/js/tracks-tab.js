/**
 * Initializes the jQuery UI components.
 */
function initTracksTab()
{
	$("#searchTrackTitle").autocomplete({
		source: "autocomplete.php?dictionary=Tracks", 
		delay: 0,
		appendTo: '#tabTracks',
		select: function(event, ui){
			trackSearchParam.TrackTitle = ui.item.label; 
			updateTrackSearchUrl(trackSearchParam); 
		}		
	})
	.on('input',function(e){
		trackSearchParam.TrackTitle = $("#searchTrackTitle").val(); 
		updateTrackSearchUrl(trackSearchParam); 
	}); 
					
	$("#searchTrackArtist").autocomplete({
		source: "autocomplete.php?dictionary=Participants",
		delay: 0,
		appendTo: '#tabTracks',
		select: function(event, ui){
			trackSearchParam.Artist = ui.item.label; 
			updateTrackSearchUrl(trackSearchParam); 
		}	
	})	
	.on('input',function(e){
		trackSearchParam.Artist = $("#searchTrackArtist").val(); 
		updateTrackSearchUrl(trackSearchParam);
	}); 
		
	$("#searchTrackAlbumTitle").autocomplete({
		source: "autocomplete.php?dictionary=Albums",
		delay: 0,
		appendTo: '#tabTracks',
		select: function(event, ui){
			trackSearchParam.AlbumTitle = ui.item.label; 
			updateTrackSearchUrl(trackSearchParam); 
		}	
	})
	.on('input',function(e){
		trackSearchParam.AlbumTitle = $("#searchTrackAlbumTitle").val(); 
		updateTrackSearchUrl(trackSearchParam);
	}); 

	$(".trackResults").flexigrid({
		url : '',
		dataType : 'json',
		colModel : [ {
			display : 'TrackID',
			name : 'trackID',
			width : 40,
			sortable : false,
			align : 'left',
			hide: true
		}, {
			display : 'AlbumID',
			name : 'albumID',
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
			width : 335,
			sortable : false,
			align : 'left'
		}, {
			display : 'Artists',
			name : 'artists',
			width : 335,
			sortable : false,
			align : 'left'
		}, {
			display : 'Track #',
			name : 'number',
			width : 100,
			sortable : false,
			align : 'left',
		}, {
			display : 'Track Length',
			name : 'length',
			width : 100,
			sortable : false,
			align : 'left',
		} ],
		sortname : "iso",
		sortorder : "asc",
		showToggleBtn : false,
		usepager : true,
		useRp : true,
		nohresize : true,
		resizable: false, 
		onDragCol: false,
		rp : 15,
		showTableToggleBtn : true,
		method : "GET",
		width : $("trackResults").innerWidth(),
		height : 200,
		singleSelect: true,
		onSuccess:function(){

			// Get album IDs
			var albumID = []; 
			$('td[abbr="albumID"]', ".trackResults").each(function() {
				albumID.push($(this).text()); 
			});
			
			// Set the selected row
			setFlexigridRow(0, ".trackResults");
			
			// Refresh album information
			refreshAlbumInformation(albumID[0], "tabTracks");

			if(albumID.length == 0)
				setStatus("tabTracksFooter", "Ready", false);						
		}
	})
	
	$(".trackResults").click(function(){
		$('.trSelected', this).each(function(){
			console.log(
					'  trackID: ' + $('td[abbr="trackID"] >div', this).html() +
					'  albumID: ' + $('td[abbr="albumID"] >div', this).html() +
					'  index: ' + $('td[abbr="index"] >div', this).html() +
					'  name: ' + $('td[abbr="name"] >div', this).html() +
					'  artists: ' + $('td[abbr="artists"] >div', this).html() +
					'  number: ' + $('td[abbr="number"] >div', this).html() +
					'  length: ' + $('td[abbr="length"] >div', this).html()
				);
			refreshAlbumInformation($('td[abbr="albumID"] >div', this).html(), "tabTracks");
		});
	});	
	
	$("#trackSearchButton").button().click(function(event) {				
			 clearAlbumInformation("tabTracks");
			 $(".trackResults").flexOptions({url: 'flexigrid.php?url=' + urlBuilder.getTrackSearchUrl(trackSearchParam) + "&depth=Names" }).flexReload(); 
			 setStatus("tabTracksFooter", "Searching...", true);
	});		
}

/**
 * Sets the track search URL.
 * @param trackSearchParam The track search parameters.
 */
function updateTrackSearchUrl(trackSearchParam) {
	document.getElementById("trackSearchURL").value = urlBuilder.getTrackSearchUrl(trackSearchParam);
}	
