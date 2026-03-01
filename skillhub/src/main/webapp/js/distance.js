var geocoder, location1, location2;

function initialize() {
    geocoder = new GClientGeocoder();
}

function showLocation() {
    var result = new Object();
    geocoder.getLocations(document.forms[0].address1.value, function (response) {
        if (!response || response.Status.code != 200) {
            alert("Sorry, we were unable to geocode the first address");
        } else {
            location1 = {
                lat: response.Placemark[0].Point.coordinates[1],
                lon: response.Placemark[0].Point.coordinates[0],
                address: response.Placemark[0].address
            };

            var address2 = document.forms[0].address2.value;
            var addresses = address2.split(',');

            for (var i = 0; i < addresses.length; i++) {
                var theAddress = addresses[i];
                geocoder.getLocations(theAddress, function (response) {
                    if (!response || response.Status.code != 200) {
                        alert("Sorry, we were unable to geocode the address: " + theAddress);
                    } else {
                        location2 = {
                            lat: response.Placemark[0].Point.coordinates[1],
                            lon: response.Placemark[0].Point.coordinates[0],
                            address: response.Placemark[0].address
                        };
                        result += calculateDistance();
                        result.push(result);
                    }
                });
            }
        }
    });
    alert("HERE NOW " + result);
    //document.getElementById('results').innerHTML =  result;
}

function calculateDistance() {
    try {
        var glatlng1 = new GLatLng(location1.lat, location1.lon);
        var glatlng2 = new GLatLng(location2.lat, location2.lon);
        var miledistance = glatlng1.distanceFrom(glatlng2, 3959).toFixed(1);
        var kmdistance = (miledistance * 1.609344).toFixed(1);
        var ret = '<strong>Address 1: </strong>' + location1.address + ' (' + location1.lat + ':' + location1.lon + ')<br /><strong>Address 2: </strong>' + location2.address + ' (' + location2.lat + ':' + location2.lon + ')<br /><strong>Distance: </strong>' + miledistance + ' miles (or ' + kmdistance + ' kilometers)';
        return ret;
    } catch (error) {
        alert(error);
    }
    return "ERROR";
}

