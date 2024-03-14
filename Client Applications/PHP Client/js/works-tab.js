/**
 * Initializes the jQuery UI components.
 */
function initWorksTab()
{
	$("#searchWorkName").autocomplete({
		source: "autocomplete.php?dictionary=Works", 
		delay: 0,
		appendTo: '#tabWorks',
		select: function(event, ui){
			workSearchParam.Name = ui.item.label; 
			updateWorkSearchUrl(workSearchParam); 
		}		
	})
	.on('input',function(e){
		workSearchParam.Name = $("#searchWorkName").val(); 
		updateWorkSearchUrl(workSearchParam); 
	}); 
					
	$("#searchWorkCatalogue").autocomplete({
		source: "autocomplete.php?dictionary=MusicCatalogues",
		delay: 0,
		appendTo: '#tabWorks',
		select: function(event, ui){
			workSearchParam.Catalogue = ui.item.label; 
			updateWorkSearchUrl(workSearchParam); 
		}	
	})	
	.on('input',function(e){
		workSearchParam.Catalogue = $("#searchWorkCatalogue").val(); 
		updateWorkSearchUrl(workSearchParam);
	}); 
		
	$("#searchWorkComposer").autocomplete({
		source: "autocomplete.php?dictionary=WorksComposers",
		delay: 0,
		appendTo: '#tabWorks',
		select: function(event, ui){
			workSearchParam.Composers = ui.item.label; 
			updateWorkSearchUrl(workSearchParam); 
		}	
	})
	.on('input',function(e){
		workSearchParam.Composers = $("#searchWorkComposer").val(); 
		updateWorkSearchUrl(workSearchParam);
	}); 

	$(".workResults").flexigrid({
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
			width : 447,
			sortable : false,
			align : 'left'
		}, {
			display : 'Composers',
			name : 'composers',
			width : 447,
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
		width : $("workResults").innerWidth(),
		height : 200,
		singleSelect: true,
		onSuccess:function(){

			// Get works IDs
			var workID = []; 
			$('td[abbr="id"]', ".workResults").each(function(){
				workID.push($(this).text()); 
			});
			
			// Set the selected row
			setFlexigridRow(0, ".workResults");
							
			// Refresh album information
			refreshWorkTrackAppearances(workID[0], "tabWorks");

			if(workID.length == 0)
			{
				$(".workTrackAppearances").flexAddData(clearFlexigrid());	
				setStatus("tabWorksFooter", "Ready", false);	
			}
		}
	});
	
	$(".workResults").click(function(){
		$('.trSelected', this).each(function(){
			console.log(
					'  id: ' + $('td[abbr="id"] >div', this).html() +
					'  index: ' + $('td[abbr="index"] >div', this).html() +
					'  name: ' + $('td[abbr="name"] >div', this).html() +
					'  composers: ' + $('td[abbr="composers"] >div', this).html()
				);
			refreshWorkTrackAppearances($('td[abbr="id"] >div', this).html(), "tabWorks");
		});
	});	
	
	$(".workTrackAppearances").flexigrid({
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
			display : 'Track Name',
			name : 'trackName',
			width : 220,
			sortable : false,
			align : 'left'
		}, {
			display : 'Artist Name',
			name : 'artistName',
			width : 220,
			sortable : false,
			align : 'left'
		}, {
			display : 'Album Name',
			name : 'albumName',
			width : 220,
			sortable : false,
			align : 'left'
		}, {
			display : 'Track Number',
			name : 'trackNumber',
			width : 100,
			sortable : false,
			align : 'left'
		}, {
			display : 'Track Length',
			name : 'trackLength',
			width : 100,
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
		width : $("workTrackAppearances").innerWidth(),
		height : 170,
		singleSelect: true
	});		
			
	$("#workSearchButton").button().click(function(event) {				
		 $(".workTrackAppearances").flexAddData(clearFlexigrid());	
		 $(".workResults").flexOptions({url: 'flexigrid.php?url=' + urlBuilder.getWorkSearchUrl(workSearchParam) + "&depth=Names" }).flexReload(); 
		 setStatus("tabWorksFooter", "Searching...", true);
	});		
}

/**
 * Makes an AJAX call to request the participant associates and displays the results.
 * @param track The Decibel participant ID.
 */		
function refreshWorkTrackAppearances(workID)
{
	// Make AJAX call to update participant information
	window.workAppearancesClient;
	setStatus("tabWorksFooter", "Searching...", true);
	if (window.XMLHttpRequest){ // code for IE7+, Firefox, Chrome, Opera, Safari
		  window.workAppearancesClient = new XMLHttpRequest();
	}
	else { // code for IE6, IE5
	  window.workAppearancesClient = new ActiveXObject("Microsoft.XMLHTTP");
	}
	window.workAppearancesClient.abort(); // Abort any pending requests
	window.workAppearancesClient.onreadystatechange = function(){
		if (window.workAppearancesClient.readyState == 4 && window.workAppearancesClient.status == 200){
			try	{
				// Parse the JSON response into an object
				var obj = jQuery.parseJSON(window.workAppearancesClient.responseText);
			}
			catch(err) {
				setStatus("tabWorksFooter", "Ready", false);
				return;
			}

			// Store the participant associates in a global variable
			window.workTrackAppearances = obj.ResultSet;
			
			// Update the participantAssociates Flexigrid 
			$(".workTrackAppearances").flexAddData(formatWorkTrackAppearances(eval(window.workTrackAppearances)));		
			setFlexigridRow(0, ".workTrackAppearances");
			
			setStatus("tabWorksFooter", "Ready", false);
		}
	}
	// Request participant associates by participant id
	window.workAppearancesClient.open("GET","request.php?search=WorkTrackAppearances&id=" + workID, true); 
	window.workAppearancesClient.send();		
}

/**
 * Converts an array of work track appearances into Flexigrid JSON format.
 * @param tracks Array of work track appearances.
 * @return The Flexigrid JSON object.
 */
function formatWorkTrackAppearances(trackAppearances){
	
	// Create a JSON representation of track appearances
	var rows = Array();
	count = 1;
	for (var i = 0; i < trackAppearances.length; i++) 
	{
		var item = trackAppearances[i];
		rows.push({ cell: [item.ID,
						   count,
						   item.TrackName,
						   item.TrackArtistName,
						   item.AlbumName,
						   item.TrackNumber,
						   formatTime(item.TotalSeconds) ]
		});	
		count++;
	}	
	return {
		 total: trackAppearances.length,
		 page: 1,
		 rows: rows
	};
}

/**
 * Sets the work search URL.
 * @param workSearchParam The search parameters.
 */
function updateWorkSearchUrl(workSearchParam) {
	document.getElementById("workSearchURL").value = urlBuilder.getWorkSearchUrl(workSearchParam);
}	
