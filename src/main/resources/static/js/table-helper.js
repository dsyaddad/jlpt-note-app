function makeResizable(table, storageKey) {
    const cols = table.querySelectorAll("th");

    // Restore widths from localStorage if available
    const saved = JSON.parse(localStorage.getItem(storageKey) || "[]");
    saved.forEach((w, i) => {
        if (w) cols[i].style.width = w + "px";
    });

    [].forEach.call(cols, function (col, index) {
        const resizer = document.createElement("div");
        resizer.classList.add("resizer");
        col.appendChild(resizer);
        resizer.addEventListener("mousedown", initResize);

        let startX, startWidth;

        function initResize(e) {
            startX = e.pageX;
            startWidth = col.offsetWidth;
            document.addEventListener("mousemove", resizeColumn);
            document.addEventListener("mouseup", stopResize);
        }

        function resizeColumn(e) {
            const newWidth = startWidth + (e.pageX - startX);
            col.style.width = newWidth + "px";
        }

        function stopResize() {
            // Save all widths to localStorage
            const widths = Array.from(cols).map(c => c.offsetWidth);
            localStorage.setItem(storageKey, JSON.stringify(widths));

            document.removeEventListener("mousemove", resizeColumn);
            document.removeEventListener("mouseup", stopResize);
        }
    });
}
document.addEventListener("DOMContentLoaded", function () {
    makeResizable(document.getElementById("resizableTable"), "mainNotesTableWidths");
    const bulkDeleteBtn = document.getElementById('bulkDeleteBtn');
    const checkboxes = document.querySelectorAll('.rowCheckbox');
    const selectAll = document.getElementById('selectAll');

    function updateButtonState() {
        const anyChecked = Array.from(checkboxes).some(cb => cb.checked);
        bulkDeleteBtn.disabled = !anyChecked;
    }

    checkboxes.forEach(cb => cb.addEventListener('change', updateButtonState));
    selectAll.addEventListener('change', function() {
        checkboxes.forEach(cb => cb.checked = selectAll.checked);
        updateButtonState();
    });

    const buttonClear = document.getElementById("buttonClear");
    const sectionInput = document.getElementById("sectionInput");
    const noteInput = document.getElementById("noteInput");
    const levelSelect = document.getElementById("levelSelect");

    buttonClear.addEventListener("click", function () {
        sectionInput.value = "";
        noteInput.value = "";
        levelSelect.value = ""; // balik ke default option
    });
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
    })
});
