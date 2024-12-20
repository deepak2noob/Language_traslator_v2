document.addEventListener('DOMContentLoaded', function() {
    const translatorForm = document.getElementById('translatorForm');
    const inputText = document.getElementById('inputText');
    const outputText = document.getElementById('outputText');
    const fromLang = document.getElementById('fromLang');
    const toLang = document.getElementById('toLang');
    
    // Move button declaration outside of try block for proper scoping
    const submitButton = translatorForm.querySelector('button[type="submit"]');
    
    translatorForm.addEventListener('submit', async function(e) {
        e.preventDefault();
        
        if (!inputText.value.trim()) {
            inputText.classList.add('is-invalid');
            return;
        }

        const originalButtonText = submitButton.innerHTML;
        try {
            // Show loading state
            submitButton.disabled = true;
            submitButton.innerHTML = 'Translating...';
            
            // Prepare the query parameters
            const params = new URLSearchParams({
                text: inputText.value.trim(),
                fromLang: fromLang.value,
                toLang: toLang.value
            });

            // Make the translation request
            const response = await fetch(`/translate?${params.toString()}`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const data = await response.json();

            // Update the output
            if (data.responseData && data.responseData.translatedText) {
                outputText.textContent = data.responseData.translatedText;
            } else {
                throw new Error('Translation failed');
            }

        } catch (error) {
            console.error('Translation error:', error);
            outputText.textContent = 'Error: Could not perform translation. Please try again.';
        } finally {
            // Reset button state
            submitButton.disabled = false;
            submitButton.innerHTML = originalButtonText;
        }
    });

    // Real-time validation for input text
    inputText.addEventListener('input', function() {
        if (this.value.trim()) {
            this.classList.remove('is-invalid');
        }
    });

    // Swap languages functionality
    const swapLangsButton = document.getElementById('swapLangs');
    if (swapLangsButton) {  // Check if button exists before adding listener
        swapLangsButton.addEventListener('click', function() {
            const fromValue = fromLang.value;
            fromLang.value = toLang.value;
            toLang.value = fromValue;
        });
    }
});