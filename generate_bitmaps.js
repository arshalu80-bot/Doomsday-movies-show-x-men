const fs = require('fs');
const { execSync } = require('child_process');

// Build an ultra-high definition SVG matching IMG_0952.jpeg exactly
const masterSvg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" width="1024" height="1024">
  <defs>
    <!-- Pitch Black Background -->
    <radialGradient id="bgGlow" cx="48%" cy="46%" r="55%">
      <stop offset="0%" stop-color="#02140d" stop-opacity="0.6"/>
      <stop offset="45%" stop-color="#05070a" stop-opacity="0.95"/>
      <stop offset="100%" stop-color="#000000"/>
    </radialGradient>

    <!-- Metallic Brushed Chrome Gradients -->
    <linearGradient id="mainMetal" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#f1f5f9"/>
      <stop offset="12%" stop-color="#cbd5e1"/>
      <stop offset="25%" stop-color="#94a3b8"/>
      <stop offset="45%" stop-color="#475569"/>
      <stop offset="65%" stop-color="#1e293b"/>
      <stop offset="80%" stop-color="#64748b"/>
      <stop offset="92%" stop-color="#94a3b8"/>
      <stop offset="100%" stop-color="#334155"/>
    </linearGradient>

    <linearGradient id="ringMetal" x1="100%" y1="0%" x2="0%" y2="100%">
      <stop offset="0%" stop-color="#e2e8f0"/>
      <stop offset="20%" stop-color="#94a3b8"/>
      <stop offset="40%" stop-color="#475569"/>
      <stop offset="60%" stop-color="#1e293b"/>
      <stop offset="80%" stop-color="#64748b"/>
      <stop offset="100%" stop-color="#334155"/>
    </linearGradient>

    <linearGradient id="bevelDark" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#0f172a"/>
      <stop offset="50%" stop-color="#020617"/>
      <stop offset="100%" stop-color="#1e293b"/>
    </linearGradient>

    <linearGradient id="bevelLight" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#ffffff"/>
      <stop offset="50%" stop-color="#cbd5e1"/>
      <stop offset="100%" stop-color="#64748b"/>
    </linearGradient>

    <!-- Emerald Green Edge Luminescence -->
    <linearGradient id="emeraldGlowGrad" x1="0%" y1="0%" x2="100%" y2="100%">
      <stop offset="0%" stop-color="#34d399"/>
      <stop offset="25%" stop-color="#00ff88"/>
      <stop offset="50%" stop-color="#10b981"/>
      <stop offset="80%" stop-color="#059669"/>
      <stop offset="100%" stop-color="#00ff88"/>
    </linearGradient>

    <linearGradient id="topGlowGrad" x1="20%" y1="0%" x2="80%" y2="100%">
      <stop offset="0%" stop-color="#00ff88"/>
      <stop offset="50%" stop-color="#10b981"/>
      <stop offset="100%" stop-color="#047857"/>
    </linearGradient>

    <filter id="softGlow" x="-30%" y="-30%" width="160%" height="160%">
      <feGaussianBlur stdDeviation="8" result="blur"/>
      <feComposite in="SourceGraphic" in2="blur" operator="over"/>
    </filter>

    <filter id="subtleGlow" x="-20%" y="-20%" width="140%" height="140%">
      <feGaussianBlur stdDeviation="3" result="blur"/>
      <feComposite in="SourceGraphic" in2="blur" operator="over"/>
    </filter>
  </defs>

  <!-- Pitch black canvas -->
  <rect width="1024" height="1024" fill="#000000"/>
  <rect width="1024" height="1024" fill="url(#bgGlow)"/>

  <!-- Base scaling & centering group for perfect icon composition -->
  <g transform="translate(512, 490) scale(4.4) translate(-54, -50)">

    <!-- EMERALD AMBIENT BACKLIGHT BEHIND THE UPPER EMBLEM -->
    <path d="M 54 18 A 36 36 0 0 0 18 54 A 36 36 0 0 0 25 74"
          fill="none" stroke="#00ff88" stroke-width="6" opacity="0.35" filter="url(#softGlow)"/>
    <path d="M 54 10 L 68 10 L 68 20"
          fill="none" stroke="#00ff88" stroke-width="7" opacity="0.4" filter="url(#softGlow)"/>

    <!-- 1. DEEP SHADOW & 3D EXTRUSION BASE LAYER -->
    <!-- Shadow offset below and right -->
    <g transform="translate(1.5, 2.5)" opacity="0.9">
      <!-- Ring shadow -->
      <path d="M 54 18 A 36 36 0 1 0 85 68 L 76 64 A 27 27 0 1 1 54 27 Z" fill="#000000"/>
      <!-- 'A' body shadow -->
      <path d="M 58 10 L 70 10 L 70 20 L 48 57 L 70 57 L 58 72 L 58 75 L 36 75 L 29 89 L 16 89 Z" fill="#000000"/>
    </g>

    <!-- 2. OUTER CIRCULAR METALLIC RING -->
    <!-- Glowing emerald rim on the outer top-left circumference -->
    <path d="M 54 17.5 A 36.5 36.5 0 1 0 85.5 68.5"
          fill="none" stroke="url(#emeraldGlowGrad)" stroke-width="2.2" filter="url(#subtleGlow)"/>

    <!-- Main Outer Ring Steel Slab -->
    <path d="M 54 18 A 36 36 0 1 0 85 68 L 76.5 63.5 A 27.5 27.5 0 1 1 54 26.5 Z"
          fill="url(#ringMetal)" stroke="#0b1120" stroke-width="1.2"/>

    <!-- Inner Ring Bevel Chamfer Highlight -->
    <path d="M 54 26.5 A 27.5 27.5 0 1 0 76.5 63.5"
          fill="none" stroke="#cbd5e1" stroke-width="0.8" opacity="0.6"/>

    <!-- Outer Ring Chamfer Highlight -->
    <path d="M 54 18 A 36 36 0 1 0 85 68"
          fill="none" stroke="#64748b" stroke-width="0.8" opacity="0.7"/>

    <!-- 3. THE AVENGERS 'A' - STRUCTURE -->

    <!-- Emerald outer glow rim on the left diagonal spine -->
    <path d="M 57.5 9 L 16 89.5 L 21 91.5 L 60.5 11 Z"
          fill="url(#emeraldGlowGrad)" filter="url(#subtleGlow)"/>

    <!-- Top Cap Emerald Highlight -->
    <path d="M 56.5 8.5 L 70.5 8.5 L 70.5 12 L 56.5 12 Z"
          fill="url(#topGlowGrad)" filter="url(#subtleGlow)"/>

    <!-- Left Leg Outer Dark Extrusion Wall -->
    <path d="M 58 10 L 69 10 L 69 20 L 48 57 L 58 57 L 70 57 L 58 72 L 58 75 L 36 75 L 29 89 L 18 89 Z"
          fill="url(#bevelDark)" stroke="#0b1120" stroke-width="1"/>

    <!-- Main 'A' Brushed Steel Face Slab -->
    <path d="M 58 10 L 68 10 L 68 20 L 48 57 L 58 57 L 70 57 L 58 72 L 58 75 L 36 75 L 29 89 L 18 89 Z"
          fill="url(#mainMetal)" stroke="#0f172a" stroke-width="0.7"/>

    <!-- Left Leg Center Chamfer Ridge (Long 3D Bevel) -->
    <path d="M 58 10 L 64 10 L 26 89 L 18 89 Z"
          fill="url(#bevelLight)" opacity="0.6"/>

    <!-- Left Leg Specular Bright Highlight Line -->
    <path d="M 58 10 L 18 89"
          fill="none" stroke="#ffffff" stroke-width="0.9" opacity="0.8"/>

    <!-- Triangular Center Cutout with Emerald Beveled Recess -->
    <path d="M 54 29 L 46 48.5 L 62 48.5 Z"
          fill="#00ff88" opacity="0.7" filter="url(#subtleGlow)"/>
    <path d="M 54 30 L 47 48 L 61 48 Z"
          fill="#05070a" stroke="#00ff88" stroke-width="1.2"/>
    <path d="M 54 33 L 49 46.5 L 59 46.5 Z"
          fill="#000000"/>

    <!-- Vertical Spine (Right of Triangle) -->
    <path d="M 58 20 L 68 20 L 68 57 L 58 57 Z"
          fill="url(#mainMetal)" stroke="#0f172a" stroke-width="0.7"/>

    <!-- 4. ARROWHEAD / HORIZONTAL BAR CUTTING THROUGH THE CIRCLE -->

    <!-- Emerald Glow Outline for Arrowhead -->
    <path d="M 57.5 47.5 L 72 58 L 57.5 68.5"
          fill="none" stroke="url(#emeraldGlowGrad)" stroke-width="2.8" stroke-linejoin="miter" filter="url(#subtleGlow)"/>

    <!-- Arrowhead Outer Metal Slab -->
    <path d="M 58 48.5 L 70.5 58 L 58 67.5 L 58 62 L 64 58 L 58 54 Z"
          fill="url(#mainMetal)" stroke="#ffffff" stroke-width="0.6"/>

    <!-- Upper Arrow Facet (High Specular Chrome Highlight) -->
    <path d="M 58 48.5 L 70.5 58 L 64 58 L 58 54 Z"
          fill="url(#bevelLight)" opacity="0.95"/>

    <!-- Lower Arrow Facet (Deep Shaded Metal) -->
    <path d="M 58 67.5 L 70.5 58 L 64 58 L 58 62 Z"
          fill="url(#bevelDark)"/>

    <!-- Arrow Center Ridge Highlight -->
    <path d="M 58 54 L 64 58 L 70.5 58"
          fill="none" stroke="#ffffff" stroke-width="0.8"/>

    <!-- 5. RIGHT LEG BOTTOM SEGMENT -->
    <path d="M 58 67.5 L 68 58 L 68 75 L 58 75 Z"
          fill="url(#ringMetal)" stroke="#0f172a" stroke-width="0.7"/>

    <!-- Bottom Right Base Step Bar -->
    <path d="M 58 72 L 70 72 L 70 75 L 58 75 Z"
          fill="#475569" stroke="#00ff88" stroke-width="0.8"/>

    <!-- 6. HIGH-PRECISION WEATHERING SCRATCHES AND SURFACE MICRO-TEXTURE -->
    <g stroke="#ffffff" stroke-linecap="round">
      <!-- Fine white steel scratches -->
      <line x1="28" y1="75" x2="36" y2="69" stroke-width="0.4" opacity="0.6"/>
      <line x1="30" y1="78" x2="34" y2="74" stroke-width="0.3" opacity="0.5"/>
      <line x1="22" y1="84" x2="27" y2="80" stroke-width="0.4" opacity="0.5"/>
      <line x1="33" y1="64" x2="43" y2="56" stroke-width="0.35" opacity="0.5"/>
      <line x1="37" y1="58" x2="45" y2="52" stroke-width="0.4" opacity="0.6"/>
      <line x1="41" y1="50" x2="47" y2="44" stroke-width="0.3" opacity="0.4"/>
      <line x1="56" y1="24" x2="63" y2="28" stroke-width="0.4" opacity="0.6"/>
      <line x1="59" y1="16" x2="66" y2="20" stroke-width="0.35" opacity="0.5"/>
      <line x1="48" y1="82" x2="55" y2="80" stroke-width="0.4" opacity="0.4"/>
      <line x1="62" y1="70" x2="67" y2="66" stroke-width="0.35" opacity="0.5"/>
      <line x1="64" y1="36" x2="68" y2="42" stroke-width="0.3" opacity="0.4"/>
      <!-- Ring scratches -->
      <line x1="28" y1="32" x2="34" y2="38" stroke-width="0.4" opacity="0.4"/>
      <line x1="38" y1="22" x2="44" y2="26" stroke-width="0.35" opacity="0.5"/>
      <line x1="68" y1="24" x2="74" y2="30" stroke-width="0.35" opacity="0.4"/>
      <line x1="74" y1="44" x2="78" y2="50" stroke-width="0.4" opacity="0.5"/>
      <line x1="72" y1="74" x2="78" y2="70" stroke-width="0.35" opacity="0.4"/>
      <line x1="42" y1="86" x2="48" y2="84" stroke-width="0.4" opacity="0.4"/>
      <!-- Green micro-scratches & edge catch-lights -->
      <line x1="24" y1="42" x2="31" y2="49" stroke="#00ff88" stroke-width="0.5" opacity="0.65"/>
      <line x1="48" y1="16" x2="53" y2="19" stroke="#00ff88" stroke-width="0.5" opacity="0.7"/>
      <line x1="50" y1="36" x2="53" y2="42" stroke="#00ff88" stroke-width="0.4" opacity="0.6"/>
    </g>

  </g>
</svg>`;

fs.writeFileSync('master_icon_1024.svg', masterSvg);
console.log('Generated master_icon_1024.svg');

// Render to high-res PNG and all Android mipmap sizes
execSync('convert -background none -density 300 master_icon_1024.svg -resize 1024x1024 /tmp/master_icon_1024.png');
console.log('Rendered /tmp/master_icon_1024.png');

const mipmapSizes = {
  'mdpi': 48,
  'hdpi': 72,
  'xhdpi': 96,
  'xxhdpi': 144,
  'xxxhdpi': 192
};

for (const [density, size] of Object.entries(mipmapSizes)) {
  const dir = `app/src/main/res/mipmap-${density}`;
  if (!fs.existsSync(dir)) fs.mkdirSync(dir, { recursive: true });

  // Create both PNG and WebP bitmaps for maximum compatibility
  const pngPath = `${dir}/ic_launcher.png`;
  const roundPngPath = `${dir}/ic_launcher_round.png`;
  const webpPath = `${dir}/ic_launcher.webp`;
  const roundWebpPath = `${dir}/ic_launcher_round.webp`;

  execSync(`convert /tmp/master_icon_1024.png -resize ${size}x${size} ${pngPath}`);
  execSync(`convert /tmp/master_icon_1024.png -resize ${size}x${size} ${roundPngPath}`);
  execSync(`convert /tmp/master_icon_1024.png -resize ${size}x${size} ${webpPath}`);
  execSync(`convert /tmp/master_icon_1024.png -resize ${size}x${size} ${roundWebpPath}`);

  console.log(`Generated mipmap-${density} at ${size}x${size}`);
}

// Also place in drawable for in-app header image
execSync('convert /tmp/master_icon_1024.png -resize 256x256 app/src/main/res/drawable/avengers_doom_icon.png');
console.log('Generated app/src/main/res/drawable/avengers_doom_icon.png');
