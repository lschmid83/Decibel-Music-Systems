<!doctype html>
<?php
	include 'admin.php';
?>
<html>
<head>
	<meta charset="utf-8">
	<title>Decibel Sample Project</title>
	<meta name="author" content="Lawrence Schmid">
	<link href="css/redmond/jquery-ui-1.9.2.custom.min.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="css/flexigrid.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<script src="js/url-builder.js"></script>
	<script src="js/jquery-1.8.3.min.js"></script>
	<script src="js/jquery-ui-1.9.2.custom.min.js"></script>
	<script type="text/javascript" src="js/flexigrid.js"></script>
	<script type="text/javascript" src="js/album-info.js"></script>
	<script type="text/javascript" src="js/albums-tab.js"></script>
	<script type="text/javascript" src="js/participants-tab.js"></script>
	<script type="text/javascript" src="js/tracks-tab.js"></script>
	<script type="text/javascript" src="js/works-tab.js"></script>
	<script type="text/javascript" src="js/util.js"></script>
	<script type="text/javascript" src="js/date.js"></script>
	<script>

	var albumSearchParam = new AlbumSearchParam();		
	var participantSearchParam = new ParticipantSearchParam();	
	var trackSearchParam = new TrackSearchParam();	
	var workSearchParam = new WorkSearchParam();	

	var urlBuilder = new DecibelUrlBuilder(<?php echo '"' . $apiAddress . '"' ?>);

	$(function() {
		$( "#tabs" ).tabs();
		initAlbumsTab();
		initParticipantsTab();
		initTracksTab();
		initAlbumInfo();
		initWorksTab();
	});
	</script>
</head>

<body>
<!-- Tabs -->
<div id="tabs" style="padding-left:5px; padding-right:5px">
	
	<ul>
		<li><a href="#tabAlbums">Albums</a></li>
		<li><a href="#tabParticipants">Participants</a></li>
		<li><a href="#tabTracks">Tracks</a></li>
		<li><a href="#tabWorks">Works</a></li>
	</ul>
	
	<?php
		require("albums-tab.html"); // Include the albums-tab layout
	?>	

	<?php
		require("participants-tab.html"); // Include the participants-tab layout
	?>		
	
	<?php
		require("tracks-tab.html"); // Include the tracks-tab layout
	?>	
	
	<?php
		require("works-tab.html"); // Include the works-tab layout
	?>	
	
</div>

<!-- Track participants dialog -->
<div id="trackParticipantsDialog" title="Track Participants" style="display: none">
	<div id="trackParticipants"> 
		<table class="trackParticipants" style="display:none;"></table>	
		<div align="right">
			<button id="okButton" style="width:80px; margin-top:10px">OK</button>
		</div>	
	</div>
</div>

<!-- Cover art dialog -->
<div id="coverArtDialog" class="coverArtDialog" style="background:url('css/images/loading.gif') no-repeat center center;" title="Cover Art">
	<img id="coverArtImage" src="" />	
</div>

</body>

</html>
