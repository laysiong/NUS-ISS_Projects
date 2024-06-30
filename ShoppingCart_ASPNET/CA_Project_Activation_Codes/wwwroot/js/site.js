window.onload = function () {
    countCart();
}

function countCart() {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/Gallery/CountCart', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Parse the response to get the count
                var count = parseInt(xhr.responseText);
                // Update the count in the HTML
                document.getElementById('CartItemCount').innerText = count;
            } else {
                console.error('Error updating cart count');
            }
        }
    };
    xhr.send();
}