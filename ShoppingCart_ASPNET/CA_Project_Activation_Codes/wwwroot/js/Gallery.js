// Please see documentation at https://docs.microsoft.com/aspnet/core/client-side/bundling-and-minification
// for details on configuring this project to bundle and minify static web assets.

// Write your JavaScript code.

function addToCart(prodId) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/Gallery/AddCart', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Update the quantity in the DOM
                countCart();
            } else {
                console.error('Error updating quantity');
            }
        }
    };
    // Construct the data to be sent
    var data = 'prodId=' + encodeURIComponent(prodId); // Adjust this line if needed

    // Send the request with the data
    xhr.send(data);
}


