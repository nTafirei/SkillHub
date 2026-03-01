var geocoder;

function initializeGClientGeocoder() {
    geocoder = new google.maps.Geocoder();
}

function getLatLon(orgType) {

    var postcode = document.getElementById('postCode').value;

    if (postcode == '' || postcode == 'undefined') {
        alert("Please enter your post code");
        return;
    }

    if (document.createForm.lat.value == '' || document.createForm.lat.value == 'undefined'
        || document.createForm.lon.value == '' || document.createForm.lon.value == 'undefined') {

        geocoder.geocode({address: postcode}, function (results, status) {
            if (status != google.maps.GeocoderStatus.OK) {
                alert("Sorry, Google Maps were unable to geocode the your address. Please try again");
            } else {
                document.createForm.lat.value = results[0].geometry.location.lat();
                document.createForm.lon.value = results[0].geometry.location.lng();
                document.createForm.submit();
            }
        });
    } else {
        document.createForm.submit();
    }
}
	
