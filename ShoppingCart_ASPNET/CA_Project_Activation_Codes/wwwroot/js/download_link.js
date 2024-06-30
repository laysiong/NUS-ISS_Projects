document.addEventListener('DOMContentLoaded', function () {
    const downloadButtons = document.querySelectorAll('.Prod_Download button');

    downloadButtons.forEach(function (button) {
        button.addEventListener('click', function (event) {
            const parentRow = event.target.closest('.purchasedisplay');

            const optionsSelect = parentRow.querySelector('select#downloadOptions');
            const singleOption = parentRow.querySelector('span#singleOption');

            let selectedOption;

            if (optionsSelect) {
                selectedOption = optionsSelect.value;
            } else if (singleOption) {
                selectedOption = singleOption.textContent.trim();
            }

            showDownloadSuccess(selectedOption);
        });
    });

    function showDownloadSuccess(selectedOption) {
        alert(`Download successful for ${selectedOption}`);
    }
});
