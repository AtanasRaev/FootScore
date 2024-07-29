const MAX_SELECTION = 11;

function limitSelection(event) {
    const checkboxes = document.querySelectorAll('.favorite-checkbox');
    const selectedCount = Array.from(checkboxes).filter(cb => cb.checked).length;
    const errorMessage = document.getElementById('error-message');

    if (selectedCount > MAX_SELECTION) {
        event.preventDefault();
        event.target.checked = false;
        errorMessage.textContent = `You can only select up to ${MAX_SELECTION} players.`;
    } else {
        errorMessage.textContent = '';
    }
}