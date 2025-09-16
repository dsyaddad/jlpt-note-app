let formulaIdx = 0;

document.addEventListener('DOMContentLoaded', () => {
    const hiddenInput = document.getElementById('combinedSection');
    const sectionInput = document.getElementById('section');
    const subSectionInput = document.getElementById('subSection');
    const combinedValue = hiddenInput.value;
    if (combinedValue) {
        const parts = combinedValue.split('-', 2); // split maksimal 2 bagian
        sectionInput.value = parts[0] || '';
        subSectionInput.value = parts[1] || '';
    }
    document.querySelector('form').addEventListener('submit', function() {
        hiddenInput.value = sectionInput.value.trim() + (subSectionInput.value.trim() ? '-' + subSectionInput.value.trim() : '');
    });
    // inisialisasi index dari DOM yang sudah di-render thymeleaf
    const blocks = document.querySelectorAll('#formulas-container .formula-block');
    formulaIdx = blocks.length;

    // set exampleIdx per formula berdasarkan jumlah example yang ada
    blocks.forEach((block, i) => {
        const exContainer = block.querySelector(`[id^="examples-container-"]`)
            || block.querySelector(`#examples-container-${i}`);
        if (exContainer) {
            exContainer.id = `examples-container-${i}`;
            exContainer.exampleIdx = exContainer.querySelectorAll('.example-block').length || 0;
        }
    });
});

function addFormula() {
    const container = document.getElementById('formulas-container');
    const i = formulaIdx;

    const div = document.createElement('div');
    div.className = 'formula-block mb-3 card';
    div.innerHTML = `
    <div class="card-header d-flex justify-content-between align-items-center">
      <h6>Formula</h6>
      <button type="button" class="btn btn-danger btn-sm" onclick="removeFormula(this)">Remove</button>
    </div>
    <div class="card-body">
      <input type="hidden" name="formulas[${i}].id">
      <input type="hidden" name="formulas[${i}].mainNoteId">
      <input type="text"   name="formulas[${i}].typeForm"     placeholder="Sub Section" class="form-control mb-1" required>
      <input type="text"   name="formulas[${i}].pattern"      placeholder="Pattern"     class="form-control mb-1" required>
      <input type="text"   name="formulas[${i}].subFunction"  placeholder="Sub Function" class="form-control mb-1">
      <input type="text"   name="formulas[${i}].subUseWhen"   placeholder="Sub Use When" class="form-control mb-1">
      <textarea            name="formulas[${i}].subNote"      placeholder="Sub Note"     class="form-control mb-1"></textarea>

      <div id="examples-container-${i}"></div>
      <button type="button" onclick="addExample(${i})" class="btn btn-success">Add Example</button>
      <hr>
    </div>
  `;
    container.appendChild(div);
    // init counter untuk examples
    document.getElementById(`examples-container-${i}`).exampleIdx = 0;
    formulaIdx++;
}

function addExample(formulaIndex) {
    const container = document.getElementById(`examples-container-${formulaIndex}`);
    if (!container) return;
    if (typeof container.exampleIdx !== 'number') container.exampleIdx = 0;
    const j = container.exampleIdx;

    const div = document.createElement('div');
    div.className = 'example-block mb-2 card';
    div.innerHTML = `
    <div class="card-header d-flex justify-content-between align-items-center">
      <h6>Example</h6>
      <button type="button" class="btn btn-danger btn-sm" onclick="removeExample(this)">Remove</button>
    </div>
    <div class="card-body">
      <input type="hidden" name="formulas[${formulaIndex}].examples[${j}].id">
      <input type="hidden" name="formulas[${formulaIndex}].examples[${j}].formulaId">
      <input type="text"   name="formulas[${formulaIndex}].examples[${j}].sampleKanji"    placeholder="Sample Kanji"     class="form-control mb-1">
      <input type="text"   name="formulas[${formulaIndex}].examples[${j}].sampleNonKanji" placeholder="Sample Non Kanji" class="form-control mb-1">
      <input type="text"   name="formulas[${formulaIndex}].examples[${j}].meaning"        placeholder="Meaning"          class="form-control mb-1">
      <input type="text"   name="formulas[${formulaIndex}].examples[${j}].note"           placeholder="Note"             class="form-control mb-1">
      <hr>
    </div>
  `;
    container.appendChild(div);
    container.exampleIdx++;
}

function removeFormula(btn) {
    const block = btn.closest('.formula-block');
    if (!block) return;
    block.remove();
    reindexFormulas();
}

function removeExample(btn) {
    const block = btn.closest('.example-block');
    if (!block) return;
    const formulaBlock = btn.closest('.formula-block');
    block.remove();
    reindexExamples(formulaBlock);
}

function reindexFormulas() {
    const formulas = document.querySelectorAll('#formulas-container .formula-block');
    formulas.forEach((block, i) => {
        // perbarui id container examples
        let exContainer = block.querySelector('[id^="examples-container-"]');
        if (!exContainer) {
            exContainer = document.createElement('div');
            exContainer.className = 'examples-container';
            block.querySelector('.card-body').insertBefore(exContainer, block.querySelector('.btn.btn-success'));
        }
        exContainer.id = `examples-container-${i}`;

        // update semua name input/textarea di level formula
        block.querySelectorAll('input[name], textarea[name]').forEach(el => {
            el.name = el.name
                .replace(/formulas\[\d+]/g, `formulas[${i}]`); // set index formula
        });

        // reindex grandchildren di dalam formula ini
        reindexExamples(block);
    });
    // set formulaIdx baru = jumlah blok sekarang
    formulaIdx = formulas.length;
}

function reindexExamples(formulaBlock) {
    if (!formulaBlock) return;
    const formulaIndex = Array.prototype.indexOf.call(
        document.querySelectorAll('#formulas-container .formula-block'),
        formulaBlock
    );
    if (formulaIndex < 0) return;

    const exBlocks = formulaBlock.querySelectorAll('.example-block');
    exBlocks.forEach((exBlock, j) => {
        exBlock.querySelectorAll('input[name], textarea[name]').forEach(el => {
            el.name = el.name
                .replace(/formulas\[\d+]/g, `formulas[${formulaIndex}]`)
                .replace(/examples\[\d+]/g, `examples[${j}]`);
        });
    });

    const container = formulaBlock.querySelector(`[id^="examples-container-"]`);
    if (container) container.exampleIdx = exBlocks.length;
}
