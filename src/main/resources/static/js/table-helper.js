document.addEventListener("DOMContentLoaded", function () {
    // Inisialisasi DataTables untuk tabel dengan ID 'resizableTable'
    new DataTable('#resizableTable', {
        "paging": false,
        "info": false,
        "lengthChange": false,
        // "scrollX": true,
        "dom": "<'row'<'col-sm-6'f>>t",
        "language": { search: "" }
    });
    $('#resizableTable_filter input[type="search"]')
        .attr('placeholder', 'Searching ...')
        .css('margin-bottom', '5px');

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
    const posSelect = document.getElementById("posSelect");


    buttonClear.addEventListener("click", function () {
        if(sectionInput){
            sectionInput.value = "";
        }
        if(noteInput){
            noteInput.value = "";
        }
        if(levelSelect){
            levelSelect.value = "";
        }
        if(posSelect){
            posSelect.value = "";
        }
    });

    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
    })

});
