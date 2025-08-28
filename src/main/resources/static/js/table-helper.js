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
});
