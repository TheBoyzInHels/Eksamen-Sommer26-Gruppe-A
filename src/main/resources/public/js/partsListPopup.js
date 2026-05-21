document.addEventListener("DOMContentLoaded", function () {

    const partsListForms = document.querySelectorAll(".partsList-form");

    partsListForms.forEach(form => {

        form.addEventListener("submit", function (event) {

            event.preventDefault();

            window.open(
                "",
                "partsListPopup",
                "width=800,height=700"
            );

            form.target = "partsListPopup";

            form.submit();
        });
    });
});