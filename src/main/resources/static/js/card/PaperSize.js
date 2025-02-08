export class PaperSize {
    static UNIT_WIDTH = 105;
    static UNIT_HEIGHT = 148;
    static SIZES = {
        A3: { width: 420, height: 297 },
        A4: { width: 210, height: 297 },
        A5: { width: 210, height: 148 },
        A6: { width: 105, height: 148 }
    };

    constructor(width, height) {
        this.width = width;
        this.height = height;
    }

    static fromName(name) {
        const size = this.SIZES[name];
        if (!size) {
            throw new Error(`Invalid paper size: ${name}`);
        }
        return size;
    }

    static random() {
        const sizes = Object.keys(this.SIZES);
        const randomName = sizes[Math.floor(Math.random() * sizes.length)];
        return this.fromName(randomName);
    }
}
