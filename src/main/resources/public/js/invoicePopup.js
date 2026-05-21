document.addEventListener("DOMContentLoaded", function () {

    const invoiceForms = document.querySelectorAll(".invoice-form");

    invoiceForms.forEach(form => {

        form.addEventListener("submit", function (event) {

            event.preventDefault();

            window.open(
                "",
                "invoicePopup",
                "width=800,height=700"
            );

            form.target = "invoicePopup";

            form.submit();
        });
    });
});