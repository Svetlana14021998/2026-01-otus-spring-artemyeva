function hashCode(str) {
    let hash = 0;
    if (!str || str.length === 0) return hash;

    for (let i = 0; i < str.length; i++) {
        const char = str.charCodeAt(i);
        hash = ((hash << 5) - hash) + char;
        hash = hash & hash;
    }
    return Math.abs(hash);
}

function generateColorFromString(text) {
    if (!text || text.trim() === '') {
        return {
            backgroundColor: '#f1f5f9',
            color: '#475569'
        };
    }

    const baseHash = hashCode(text);

    const words = text.trim().split(/\s+/);
    const wordCount = words.length;

    const letterCount = text.replace(/[^a-zA-Zа-яА-Я]/g, '').length;

    const upperCount = (text.match(/[A-ZА-Я]/g) || []).length;

    const combinedHash = baseHash + (wordCount * 31) + (letterCount * 17) + (upperCount * 7);
    const finalHash = hashCode(combinedHash.toString());

    const hue = finalHash % 360;

    const saturation = 70 + (finalHash % 25);

    const lightness = 45 + (finalHash % 20);

    const backgroundColor = `hsl(${hue}, ${saturation}%, ${lightness}%)`;

    const textColor = lightness > 55 ? '#1e293b' : '#ffffff';

    return {
        backgroundColor: backgroundColor,
        color: textColor
    };
}

function applyBadgeStyle(element, text) {
    if (!element) return;

    const style = generateColorFromString(text || element.textContent.trim());
    element.style.backgroundColor = style.backgroundColor;
    element.style.color = style.color;
}

function initBadges() {
    document.querySelectorAll('.badge-hash').forEach(element => {
        const text = element.dataset.text || element.textContent.trim();
        if (text) {
            applyBadgeStyle(element, text);
        }
    });
}

function generateBadgeStyle(text) {
    const style = generateColorFromString(text);
    return `background-color: ${style.backgroundColor}; color: ${style.color};`;
}