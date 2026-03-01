var rendererOptions = {
    draggable: true
};

var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
var directionsService = new google.maps.DirectionsService();

function initializeMap() {

    var lat = document.getElementById('lat').value;
    var lon = document.getElementById('lon').value;
    var mapTitle = document.getElementById('mapTitle').value;
    var destinationAddress = document.getElementById('toAddress').value;

    var sourceLatLong = new google.maps.LatLng(lat, lon);

    var mapOptions = {
        zoom: 8,
        center: sourceLatLong
    };

    var mapCanvas = document.getElementById('map_canvas');
    var map = new google.maps.Map(mapCanvas, mapOptions);

    //google.maps.event.addListener(directionsDisplay, 'directions_changed', function () {
    //  computeTotalDistance(directionsDisplay.getDirections());
    //});

    calcRoute(sourceLatLong, destinationAddress);

    directionsDisplay.setMap(map);
    directionsDisplay.setPanel(document.getElementById('directions_panel'));
}

function calcRoute(sourceLatLong, destinationAddress) {

    var request = {
        origin: sourceLatLong,
        destination: destinationAddress,
        travelMode: google.maps.TravelMode.DRIVING
    };
    directionsService.route(request, function (response, status) {
        if (status == google.maps.DirectionsStatus.OK) {
            directionsDisplay.setDirections(response);
        } else if (status == 'ZERO_RESULTS') {
            alert('No route could be found between the origin and destination.');
        } else if (status == 'UNKNOWN_ERROR') {
            alert('A directions request could not be processed due to a server error. The request may succeed if you try again.');
        } else if (status == 'REQUEST_DENIED') {
            alert('This webpage is not allowed to use the directions service.');
        } else if (status == 'OVER_QUERY_LIMIT') {
            alert('The webpage has gone over the requests limit in too short a period of time.');
        } else if (status == 'NOT_FOUND') {
            alert('At least one of the origin, destination, or waypoints could not be geocoded.');
        } else if (status == 'INVALID_REQUEST') {
            alert('The DirectionsRequest provided was invalid.');
        } else {
            alert("There was an unknown error in your request. Request status: " + status);
        }
    });
}

function computeTotalDistance(result) {
    var total = 0;
    var myroute = result.routes[0];
    for (var i = 0; i < myroute.legs.length; i++) {
        total += myroute.legs[i].distance.value;
    }
    total = total / 1000.0;
    document.getElementById('total_distance').innerHTML = 'Total Distance: ' + total + ' km';
}
