/**
 * Initializes the jQuery UI components.
 */
function initParticipantsTab()
{
	$("#searchParticipantName").autocomplete({
		source: "autocomplete.php?dictionary=Participants", 
		delay: 0,
		appendTo: '#tabParticipants',
		select: function(event, ui){
			participantSearchParam.Name = ui.item.label; 
			updateParticipantSearchUrl(participantSearchParam);
		}		
	})
	.on('input',function(e){
		participantSearchParam.Name = $("#searchParticipantName").val(); 
		updateParticipantSearchUrl(participantSearchParam); 
	}); 
					
	$("#searchParticipantActivity").autocomplete({
		source: "autocomplete.php?dictionary=Activities",
		delay: 0,
		appendTo: '#tabParticipants',
		select: function(event, ui){
			participantSearchParam.Activity = ui.item.label; 
			updateParticipantSearchUrl(participantSearchParam); 
		}	
	})	
	.on('input',function(e){
		participantSearchParam.Activity = $("#searchParticipantActivity").val(); 
		updateParticipantSearchUrl(participantSearchParam);
	}); 
		
	$("#searchParticipantDateBorn").datepicker({
		dateFormat: 'dd/mm/yy',
		onSelect: function(dateText, inst) { 
			participantSearchParam.DateBorn = getFullDate(dateText); 
			updateParticipantSearchUrl(participantSearchParam); 
		}
	})
	.focusout(function() {
		var date = getFullDate(document.getElementById("searchParticipantDateBorn").value);
		if(date == null)
			document.getElementById("searchParticipantDateBorn").value = null;
		participantSearchParam.DateBorn = date;
		updateParticipantSearchUrl(participantSearchParam); 
	});		
	
	
	$("#searchParticipantDateDied").datepicker({
		dateFormat: 'dd/mm/yy',
		onSelect: function(dateText, inst) { 
			participantSearchParam.DateDied = getFullDate(dateText); 
			updateParticipantSearchUrl(participantSearchParam); 
		}
	})
	.focusout(function() {
		var date = getFullDate(document.getElementById("searchParticipantDateDied").value);
		if(date == null)
			document.getElementById("searchParticipantDateDied").value = null;	
		participantSearchParam.DateDied = getFullDate(document.getElementById("searchParticipantDateDied").value); 
		updateParticipantSearchUrl(participantSearchParam); 
	});		
				
	$(".participantResults").flexigrid({
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
			align : 'left'
		}, {
			display : 'Name',
			name : 'name',
			width : 350,
			sortable : true,
			align : 'left'
		}, {
			display : 'Gender',
			name : 'gender',
			width : 120,
			sortable : true,
			align : 'left'
		}, {
			display : 'Date Born',
			name : 'dateBorn',
			width : 200,
			sortable : true,
			align : 'left',
		}, {
			display : 'Date Died',
			name : 'dateDied',
			width : 200,
			sortable : true,
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
		width : $("participantResults").innerWidth(),
		height : 200,
		singleSelect: true,
		onSuccess:function(){

			// Get participant IDs
			var participantID = []; 
			$('td[abbr="id"]', ".participantResults").each(function() {
				participantID.push($(this).text()); 
			});
			var participantName = []; 
			$('td[abbr="name"] >div', ".participantResults").each(function() {
				participantName.push($(this).text()); 
			});			
			
			// Set the selected row
			setFlexigridRow(0, ".participantResults");
			
			// Refresh participant associates information
			refreshParticipantAssociates(participantID[0]);
			if(participantID.length > 0)
				document.getElementById("trackAppearancesHeader").innerHTML = "Track Appearances" + " - " + participantName[0];
			else
			{
				clearParticipantInformation();
				setStatus("tabParticipantsFooter", "Ready", false);		
			}
		}
	})
	
	$(".participantResults").click(function(){
		clearParticipantInformation();
		$('.trSelected', this).each( function(){
			console.log(
					'  id: ' + $('td[abbr="id"] >div', this).html() +
					'  index: ' + $('td[abbr="index"] >div', this).html() +
					'  name: ' + $('td[abbr="name"] >div', this).html() +
					'  gender: ' + $('td[abbr="gender"] >div', this).html() +
					'  dateBorn: ' + $('td[abbr="dateBorn"] >div', this).html() +
					'  dateDied: ' + $('td[abbr="dateDied"] >div', this).html()
				);
			refreshParticipantAssociates($('td[abbr="id"] >div', this).html());
			document.getElementById("trackAppearancesHeader").innerHTML = "Track Appearances" + " - " + $('td[abbr="name"] >div', this).html();
		});

	});	
	
	$(".participantAssociates").flexigrid({
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
			display : 'Name',
			name : 'name',
			width : 245,
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
		width : $("participantAssociates").innerWidth(),
		height : 170,
		singleSelect: true,
	})	
	
	$(".participantAssociates").click(function(){
		$(".participantTrackAppearances").flexAddData(clearFlexigrid());	
		$('.trSelected', this).each(function(){
			console.log(
					'  id: ' + $('td[abbr="id"] >div', this).html() +
					'  name: ' + $('td[abbr="name"] >div', this).html()
				);
			refreshParticipantTrackAppearances($('td[abbr="id"] >div', this).html());	
			document.getElementById("trackAppearancesHeader").innerHTML = "Track Appearances" + " - " + $('td[abbr="name"] >div', this).html();
		});
	});	
			
	$(".participantTrackAppearances").flexigrid({
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
			display : 'Track Title',
			name : 'trackTitle',
			width : 220,
			sortable : false,
			align : 'left'
		}, {
			display : 'Album',
			name : 'album',
			width : 220,
			sortable : false,
			align : 'left'
		}, {		
			display : 'Activity',
			name : 'activity',
			width : 160,
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
		width : $("participantTrackAppearances").innerWidth(),
		height : 170,
		singleSelect: true,
	})			
			
	$("#participantSearchButton").button().click(function(event) {				
		 clearParticipantInformation();
		 $(".participantResults").flexOptions({url: 'flexigrid.php?url=' + urlBuilder.getParticipantSearchUrl(participantSearchParam) + "&depth=Names" }).flexReload(); 
		 setStatus("tabParticipantsFooter", "Searching...", true);
	});		
}

/**
 * Makes an AJAX call to request the participant associates and displays the results.
 * @param track The Decibel participant ID.
 */		
function refreshParticipantAssociates(participantID)
{
	// Make AJAX call to update participant information
	setStatus("tabParticipantsFooter", "Searching...", true);
	if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
		  window.associatesClient = new XMLHttpRequest();
	}
	else { // code for IE6, IE5
	  window.associatesClient = new ActiveXObject("Microsoft.XMLHTTP");
	}
	window.associatesClient.abort(); // Abort any pending requests
	window.associatesClient.onreadystatechange = function() {
		if (window.associatesClient.readyState == 4 && window.associatesClient.status == 200) {
				
			try	{
				// Parse the JSON response into an object
				var obj = jQuery.parseJSON(window.associatesClient.responseText);
			}
			catch(err) {
				setStatus("tabParticipantsFooter", "Ready", false);
				return;
			}
			
			// Store the participant associates in a global variable
			window.participantAssociates = obj.ResultSet;
			
			// Update the participantAssociates Flexigrid 
			$(".participantAssociates").flexAddData(formatParticipantAssociates(eval(window.participantAssociates)));		
						
			// Refresh participant track appearances			
			refreshParticipantTrackAppearances(participantID);			
		}
	}
	// Request participant associates by participant id
	window.associatesClient.open("GET","request.php?search=ParticipantAssociates&id=" + participantID, true); 
	window.associatesClient.send();		
}

/**
 * Converts an array of participant associates into Flexigrid JSON format.
 * @param participantAssociates Array of participant asscoiates.
 * @return The Flexigrid JSON object.
 */
function formatParticipantAssociates(participantAssociates){
	
	// Create a JSON representation of participant associates
	var rows = Array();
	count = 1;
	for (var i = 0; i < participantAssociates.length; i++) {
		var item = participantAssociates[i];
		if(item.Participant.Name.length > 0) {
			rows.push({ cell: [item.Participant.ID,
							   item.Participant.Name ]
			});	
			count++;
		}
	}	
	return {
		 total: participantAssociates.length,
		 page: 1,
		 rows: rows
	};
}

/**
 * Makes an AJAX call to request the participant track appearances and displays the results.
 * @param track The Decibel participant ID.
 */		
function refreshParticipantTrackAppearances(participantID)
{
	// Make AJAX call to update participant track appearances information
	setStatus("tabParticipantsFooter", "Searching...", true);
	if (window.XMLHttpRequest) { // code for IE7+, Firefox, Chrome, Opera, Safari
		  window.participantAppearancesClient = new XMLHttpRequest();
	}
	else { // code for IE6, IE5
	  window.participantAppearancesClient = new ActiveXObject("Microsoft.XMLHTTP");
	}
	window.participantAppearancesClient.abort(); // Abort any pending requests
	window.participantAppearancesClient.onreadystatechange = function() {
		if (window.participantAppearancesClient.readyState == 4 && window.participantAppearancesClient.status == 200) {
	
			try	{
				// Parse the JSON response into an object
				var obj = jQuery.parseJSON(window.participantAppearancesClient.responseText);
			}
			catch(err) {
				setStatus("tabParticipantsFooter", "Ready", false);
				return;
			}
			
			// Store the participant associates in a global variable
			window.participantTrackAppearances = obj.ResultSet;
			
			// Update the participantTrackAppearances Flexigrid 
			$(".participantTrackAppearances").flexAddData(formatParticipantTrackAppearances(eval(window.participantTrackAppearances)));		
			setFlexigridRow(0, ".participantTrackAppearances");							
										
			setStatus("tabParticipantsFooter", "Ready", false);
		}
	}
	// Request participant associates by participant id
	window.participantAppearancesClient.open("GET","request.php?search=ParticipantTrackAppearances&id=" + participantID, true); 
	window.participantAppearancesClient.send();		
}

/**
 * Converts an array of track appearances into Flexigrid JSON format.
 * @param tracks Array of track appearances.
 * @return The Flexigrid JSON object.
 */
function formatParticipantTrackAppearances(trackAppearances){
	
	// Create a JSON representation of track appearances
	var rows = Array();
	count = 1;
	for (var i = 0; i < trackAppearances.length; i++) {
		var item = trackAppearances[i];
		rows.push({ cell: [item.ID,
						   count,
						   item.TrackName,
						   item.AlbumName,
						   item.ActivityName ]
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
 * Gets the full date with month name from a string.
 */
function getFullDate(dateText)
{
	var d = Date.parseExact(dateText, ["d/M/yyyy", "d/M/yy"]);
	if(d == "Invalid Date" || d == null)
		return;
	var monthNames = [ "January", "February", "March", "April", "May", "June",
					   "July", "August", "September", "October", "November", "December" ];
	return d.getDate() + ' ' + monthNames[d.getMonth()] + ' ' + d.getFullYear();
}

/**
 * Clears the participant information.
 */		
function clearParticipantInformation()
{
	document.getElementById("trackAppearancesHeader").innerHTML = "Track Appearances";
	$(".participantAssociates").flexAddData(clearFlexigrid());	
	$(".participantTrackAppearances").flexAddData(clearFlexigrid());		
}

/**
 * Sets the participant search URL.
 * @param participantSearchParam The participant search parameters.
 */
function updateParticipantSearchUrl(participantSearchParam) {
	document.getElementById("participantSearchURL").value = urlBuilder.getParticipantSearchUrl(participantSearchParam);
}	
