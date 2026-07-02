import { useEffect, useRef, useState } from "react";
import "../../assets/css/warehouse/BarcodeScannerBox.css";

export default function BarcodeScannerBox({
                                              onScan,
                                              loading = false,
                                          }) {
    const [barcode, setBarcode] = useState("");

    const inputRef = useRef(null);

    useEffect(() => {
        inputRef.current?.focus();
    }, []);

    const playBeep = () => {
        try {
            const ctx = new AudioContext();
            const osc = ctx.createOscillator();
            const gain = ctx.createGain();

            osc.frequency.value = 900;
            gain.gain.value = 0.12;

            osc.connect(gain);
            gain.connect(ctx.destination);

            osc.start();

            setTimeout(() => {
                osc.stop();
                ctx.close();
            }, 120);
        } catch (e) {
            console.log(e);
        }
    };

    const handleSubmit = async (code) => {
        if (!code.trim()) return;

        try {
            await onScan?.({
                barcode: code.trim(),
                quantity: 1,
            });

            playBeep();

            setBarcode("");

            setTimeout(() => {
                inputRef.current?.focus();
            }, 50);
        } catch (error) {
            console.log(error);
        }
    };

    const handleKeyDown = async (e) => {
        if (e.key === "Enter") {
            e.preventDefault();

            await handleSubmit(barcode);
        }
    };

    return (
        <div className="barcode-scanner-panel">
            <div className="barcode-scanner-title">
                Scan Product Barcode
            </div>

            <input
                ref={inputRef}
                value={barcode}
                onChange={(e) => setBarcode(e.target.value)}
                onKeyDown={handleKeyDown}
                className="barcode-input"
                placeholder="Đưa máy quét vào mã vạch..."
                autoFocus
                disabled={loading}
            />

        </div>
    );
}