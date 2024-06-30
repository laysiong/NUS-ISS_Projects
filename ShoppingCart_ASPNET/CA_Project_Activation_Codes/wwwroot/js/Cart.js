window.onload = function () {
    updateTotalSumWithAjax();
    updateCartItemCount();
}


//Remove Product in Cart
function removeProduct(tranId, prodId) {
    //console.log(prodId)
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/Cart/RemoveProduct', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                //console.log('Product removed successfully');
                // Remove product div by ID
                var purchasedisplayDiv = document.querySelector('.purchasedisplay[data-tranId="product_' + prodId + '"]');

                if (purchasedisplayDiv) {
                    purchasedisplayDiv.parentNode.removeChild(purchasedisplayDiv);
                    updateTotalSumWithAjax();
                }
            } else {
                console.error('Error removing product');
            }
        }
    };
    xhr.send('tranId=' + encodeURIComponent(tranId) + '&prodId=' + encodeURIComponent(prodId));
}


//Update Quantity in Cart
function updateQuantity(tranId, prodId, quantity) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/Cart/UpdateQuantity', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                // Update the quantity in the DOM
                var quantityInput = document.getElementById('postfix_' + tranId + '_' + prodId);
                if (quantityInput) {
                    quantityInput.value = quantity;
                    updateTotalSumWithAjax();
                }
            } else {
                console.error('Error updating quantity');
            }
        }
    };
    xhr.send('tranId=' + encodeURIComponent(tranId) + '&prodId=' + encodeURIComponent(prodId) + '&quantity=' + encodeURIComponent(quantity));
}



// Plus button
function increaseQuantity(tranId, prodId) {
    var quantityInput = document.getElementById('postfix_' + tranId + '_' + prodId);
    if (quantityInput) {
        var quantity = parseInt(quantityInput.value) + 1;
        quantityInput.value = quantity;
        updateQuantity(tranId, prodId, quantity);
    }
}

// Minus button
function decreaseQuantity(tranId, prodId) {
    var quantityInput = document.getElementById('postfix_' + tranId + '_' + prodId);
    if (quantityInput) {
        var quantity = parseInt(quantityInput.value);
        // quantity at least 1
        if (quantity >= 2) {
            quantityInput.value = quantity - 1;
            updateQuantity(tranId, prodId, quantity - 1);

        } else {
            alert('Quantity cannot be zero');
        }
    }
}

// Function to prevent form submit by Enter
// During our demo, we realise that as user, we tend to press enter when key in
// the quantity. So we disable enter to submit form.
document.getElementById("checkoutForm").addEventListener("keypress", handleKeyPress);
function handleKeyPress(event) {
    if (event.keyCode === 13) {
        event.preventDefault();
    }
}


function handleClick(tranId, prodId) {
    var quantityInput = document.getElementById('postfix_' + tranId + '_' + prodId);
    if (quantityInput) {
        

        originalQuantity = quantityInput.value;

        // Store the original quantity value when the input is focused
        quantityInput.addEventListener('focus', function () {
            originalQuantity = quantityInput.value;
        });

        // Handle the blur event
        quantityInput.addEventListener('blur', function () {
            // Get the updated quantity from the input field
            var quantity = parseInt(quantityInput.value);
            vertifyValue(tranId, prodId, quantity, quantityInput);
        });

        //Handle keypress
        quantityInput.addEventListener("keypress", function () {
            if (event.keyCode === 13) {
                var quantity = parseInt(quantityInput.value);
                vertifyValue(tranId, prodId, quantity, quantityInput);
                quantityInput.blur();
            }
        });
    }
}


function vertifyValue(tranId, prodId, quantity, quantityInput) {
    // Check if the quantity is a valid positive number
    if (!isNaN(quantity) && quantity > 0) {
        updateQuantity(tranId, prodId, quantity);

    } else if (quantity === 0) {
        quantityInput.value = originalQuantity;
        alert('Quantity cannot be zero');

    } else {
        quantityInput.value = originalQuantity;
        alert('Please enter a valid quantity.');
    }
}


//
var inputFields = document.querySelectorAll('input[type="text"].form-control');
inputFields.forEach(function (input) {
    var idParts = input.id.split('_');
    var tranId = idParts[1];
    var prodId = idParts[2];

    handleClick(tranId, prodId);
});



/// updateTotal 
function updateTotalSumWithAjax() {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', '/Cart/GetCartTotalSum', true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                var totalSum = JSON.parse(xhr.responseText);
                let cartHTML = document.getElementById('TotalSumValue');
                let checkOut = document.getElementById('CheckOut');
                let emptyHTML = document.getElementById('CartEmpty');
                let emptyImgHTML = document.getElementById('CartEmptyImg');


                if (totalSum === 0) {
                    //CartEmpty message
                    cartHTML.innerHTML = " ";
                    emptyHTML.classList.remove("hide"); 
                    emptyImgHTML.classList.remove("hide");
                    checkOut.classList.add("disabled", "hide");
                    updateCartItemCount();

                   // console.log("Hide");
                } else {
                    //Items in Cart
                    cartHTML.innerHTML = "Total Amount: $" + totalSum.toFixed(2);
                    emptyHTML.classList.add("hide");
                    emptyImgHTML.classList.add("hide");
                    checkOut.classList.remove("disabled", "hide");
                    updateCartItemCount();

                    //console.log("Unhide");
                }
          
            } else {
                console.error('Error updating total sum');
            }
        }
    };
    xhr.send();
}

function updateCartItemCount() {
    var cartItems = document.querySelectorAll('.cartQuantity input[type="text"]');
    var totalItemCount = 0;

    cartItems.forEach(function (item) {
        totalItemCount += parseInt(item.value);
    });


    document.getElementById('CartItemCount').innerText = totalItemCount != 0 ? totalItemCount: 0;
}




//// CHECK OUT

//Handle form submission and checkout response

function handleCheckoutResponse(event) {
    // Prevent the default form submission
    event.preventDefault();

    var formData = new FormData(document.getElementById('checkoutForm'));
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/Cart/CheckOut', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {

                // Check if the response is a redirection page
                var contentType = xhr.getResponseHeader("Content-Type");
                if (contentType && contentType.indexOf("text/html") !== -1) {
                    window.location.href = '/Home/Index';
                    return;
                }

                var data = JSON.parse(xhr.responseText);

                if (data.success) {
                    alert("Transaction successful!\nThank you " + data.customerName + " for the support!");
                    //window.location.replace("https://localhost:7037/Purchases");
                    window.location.href = "/Purchases";
                    conesole.log("Purchases page");

                } else {
                    alert("Checkout failed. Our staff is looking on it.\n Please try again later.");
                }
            } else {
                alert("Error during checkout. Please try again later.");
            }
        }
    };

    xhr.send(new URLSearchParams(formData).toString());
}

document.getElementById('checkoutForm').addEventListener('submit', handleCheckoutResponse);
