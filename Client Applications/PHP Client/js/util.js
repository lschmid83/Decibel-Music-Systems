/**
 * Sets the status text and visibility of the loading animation.
 * @param text The status text.
 * @param showProgress The visibility of the loading animation.
 */
function setStatus(footerID, text, showProgress)
{
	if(document.getElementById(footerID))
	{
		var statusProgress = document.getElementById(footerID).getElementsByTagName("img")[0];
		var statusText =  document.getElementById(footerID).getElementsByTagName("div")[0];

		if(showProgress)
		{
			statusProgress.style.visibility = "visible";
			statusProgress.style.width = "16px";
			statusProgress.style.height = "16px";
			statusText.style.paddingLeft = "5px";
		}
		else
		{
			statusProgress.style.visibility = "hidden";
			statusProgress.style.width = "0px";
			statusProgress.style.height = "16px";
			statusText.style.paddingLeft = "0px";	
		}		
		statusText.innerHTML = text;	
	}
}

/**
 * Converts seconds into hh:mm:ss.
 * @param d Total number of seconds.
 * @return Seconds converted to a string in the format hh:mm:ss.
 */		
function formatTime(d) 
{
	d = Number(d);
	var h = Math.floor(d / 3600);
	var m = Math.floor(d % 3600 / 60);
	var s = Math.floor(d % 3600 % 60);
	return ((h > 0 ? h + ":" : "") + (m > 0 ? (h > 0 && m < 10 ? "0" : "") + m + ":" : "0:") + (s < 10 ? "0" : "") + s); 
}

/**
 * Sets the selected row in a Flexigrid.
 * @param rowNumber The row number to select.
 * @param flexigrid The Flexigrid component.
 */	
function setFlexigridRow(rowNumber, flexigrid)
{
	// Clear all selections
	$('.trSelected', flexigrid).each(function () {
		$(this).removeClass('trSelected');
	});
	
	// Set selected row
	$("tr", flexigrid).eq(rowNumber).each(function() {
		$(this).toggleClass('trSelected');	
	});	
}

/**
 * Returns an empty Flexigrid table model.
 * @return Empry Flexigrid table model.
 */	
function clearFlexigrid()
{
	return {
		 total: 0,
		 page: 1,
		 rows: null
	};
}
