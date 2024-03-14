/**
 * Initializes the jQuery UI components.
 */
function initAlbumsTab()
{
	$("#searchAlbumTitle").autocomplete({
		source: "autocomplete.php?dictionary=Albums",
		delay: 0,
		appendTo: '#tabAlbums',
		select: function(event, ui){
			albumSearchParam.AlbumTitle = ui.item.label;
			updateAlbumSearchUrl(albumSearchParam); 
		}		
	})
	.on('input',function(e){
		albumSearchParam.AlbumTitle = $("#searchAlbumTitle").val(); 
		updateAlbumSearchUrl(albumSearchParam); 
	}); 
					
	$("#searchAlbumGenre").autocomplete({
		source: "autocomplete.php?dictionary=MusicGenres",
		delay: 0,
		appendTo: '#tabAlbums',
		select: function(event, ui){
			albumSearchParam.Genre = ui.item.label; 
			updateAlbumSearchUrl(albumSearchParam); 
		}	
	})	
	.on('input',function(e){
		albumSearchParam.Genre = $("#searchAlbumGenre").val(); 
		updateAlbumSearchUrl(albumSearchParam);
	}); 
		
	$("#searchAlbumLabel").autocomplete({
		source: "autocomplete.php?dictionary=Publishers",
		delay: 0,
		appendTo: '#tabAlbums',
		select: function(event, ui){
			albumSearchParam.Label = ui.item.label; 
			updateAlbumSearchUrl(albumSearchParam); 
		}	
	})
	.on('input',function(e){
		albumSearchParam.Label = $("#searchAlbumLabel").val(); 
		updateAlbumSearchUrl(albumSearchParam);
	}); 

	$("#searchAlbumBarcode").autocomplete({
		source: "autocomplete.php?dictionary=Barcodes",
		delay: 0,
		appendTo: '#tabAlbums',
		select: function(event, ui){
			albumSearchParam.Barcode = ui.item.label; 
			updateAlbumSearchUrl(albumSearchParam); 
		}	
	})
	.on('input',function(e){
		albumSearchParam.Barcode = $("#searchAlbumBarcode").val(); 
		updateAlbumSearchUrl(albumSearchParam);
	});
	
	$("#searchAlbumArtistName").autocomplete({
		source: "autocomplete.php?dictionary=Participants",
		delay: 0,
		appendTo: '#tabAlbums',
		select: function(event, ui){
			albumSearchParam.ArtistName = ui.item.label; 
			updateAlbumSearchUrl(albumSearchParam); 
		}	
	})
	.on('input',function(e){
		albumSearchParam.ArtistName = $("#searchAlbumArtistName").val(); 
		updateAlbumSearchUrl(albumSearchParam);
	});

	$("#searchAlbumRegion").autocomplete({
		source: "autocomplete.php?dictionary=GeoEntities",
		delay: 0,
		appendTo: '#tabAlbums',
		select: function(event, ui){
			albumSearchParam.Region = ui.item.label; 
			updateAlbumSearchUrl(albumSearchParam); 
		}	
	})
	.on('input',function(e){
		albumSearchParam.Region = $("#searchAlbumRegion").val(); 
		updateAlbumSearchUrl(albumSearchParam);
	});	

	$(".albumResults").flexigrid({
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
			width : 230,
			sortable : false,
			align : 'left'
		}, {
			display : 'Artists',
			name : 'artists',
			width : 230,
			sortable : false,
			align : 'left'
		}, {
			display : 'Label',
			name : 'label',
			width : 198,
			sortable : false,
			align : 'left',
		}, {
			display : 'Length',
			name : 'length',
			width : 100,
			sortable : false,
			align : 'left',
		}, {				
			display : 'Track Count',
			name : 'trackCount',
			width : 100,
			sortable : false,
			align : 'left'
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
		width : $("albumResults").innerWidth(),
		height : 200,
		singleSelect: true,
		onSuccess:function(){

			// Get album IDs
			var albumID = []; 
			$('td[abbr="id"]', ".albumResults").each(function() {
				albumID.push($(this).text()); 
			});
			
			// Set the selected row
			setFlexigridRow(0, ".albumResults");
			
			// Refresh album information
			refreshAlbumInformation(albumID[0], "tabAlbums");

			if(albumID.length == 0)
				setStatus("tabAlbumsFooter", "Ready", false);				
		}
	})
	
	$(".albumResults").click(function(){
		$('.trSelected', this).each( function(){
			console.log(
					'  id: ' + $('td[abbr="id"] >div', this).html() +
					'  index: ' + $('td[abbr="index"] >div', this).html() +
					'  name: ' + $('td[abbr="name"] >div', this).html() +
					'  artists: ' + $('td[abbr="artists"] >div', this).html() +
					'  label: ' + $('td[abbr="label"] >div', this).html() +
					'  length: ' + $('td[abbr="length"] >div', this).html() +
					'  trackCount: ' + $('td[abbr="trackCount"] >div', this).html() 
				);
			refreshAlbumInformation($('td[abbr="id"] >div', this).html(), "tabAlbums");
		});
	});	
	
	$("#albumSearchButton").button().click(function(event){				
		 clearAlbumInformation("tabAlbums");
		 $(".albumResults").flexOptions({url: "flexigrid.php?url=" + urlBuilder.getAlbumSearchUrl(albumSearchParam) + "&depth=Publications"}).flexReload(); 
		 setStatus("tabAlbumsFooter", "Searching...", true);
	});		
}

/**
 * Sets the album search URL.
 * @param albumSearchParam The album search parameters.
 */
function updateAlbumSearchUrl(albumSearchParam) {
	document.getElementById("albumSearchURL").value = urlBuilder.getAlbumSearchUrl(albumSearchParam);
}	
