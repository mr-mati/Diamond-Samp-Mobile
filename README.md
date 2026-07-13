<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Diamond SAMP Mobile · README</title>
  <style>
    /* ── reset & base ── */
    * {
      margin: 0;
      padding: 0;
      box-sizing: border-box;
    }

    body {
      background: #0d0d1a;
      font-family: 'Segoe UI', Roboto, system-ui, -apple-system, sans-serif;
      color: #e4e4ef;
      padding: 2rem 1.5rem;
      line-height: 1.6;
    }

    .container {
      max-width: 1100px;
      margin: 0 auto;
      background: #141425;
      border-radius: 32px;
      padding: 2.5rem 2.8rem;
      box-shadow: 0 20px 50px rgba(0, 0, 0, 0.8);
      border: 1px solid #2a2a4a;
    }

    /* ── header / banner ── */
    .banner {
      background: linear-gradient(145deg, #1a1a2e, #23234a);
      border-radius: 24px;
      padding: 2rem 2rem;
      text-align: center;
      margin-bottom: 2.5rem;
      border-bottom: 3px solid #7c5cbf;
      box-shadow: inset 0 0 30px rgba(90, 60, 180, 0.15);
    }

    .banner h1 {
      font-size: 3.2rem;
      font-weight: 800;
      letter-spacing: 2px;
      background: linear-gradient(135deg, #c0a0ff, #7c5cbf);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      text-shadow: 0 0 30px rgba(124, 92, 191, 0.3);
    }

    .banner .sub {
      font-size: 1.2rem;
      color: #a9a9d0;
      margin-top: 0.3rem;
      font-weight: 300;
    }

    .badge-row {
      display: flex;
      flex-wrap: wrap;
      justify-content: center;
      gap: 0.8rem 1.5rem;
      margin-top: 1.2rem;
    }

    .badge {
      background: #22224a;
      padding: 0.4rem 1.4rem;
      border-radius: 60px;
      font-size: 0.85rem;
      font-weight: 600;
      color: #b4b4f0;
      border: 1px solid #3d3d6b;
      letter-spacing: 0.5px;
    }

    .badge i {
      font-style: normal;
      margin-right: 6px;
    }

    /* ── typography ── */
    h2 {
      font-size: 1.8rem;
      font-weight: 700;
      margin: 2.2rem 0 1rem 0;
      padding-bottom: 0.4rem;
      border-bottom: 2px solid #2d2d55;
      color: #d4d4ff;
    }

    h3 {
      font-size: 1.3rem;
      font-weight: 600;
      margin: 1.6rem 0 0.6rem 0;
      color: #c8c8f0;
    }

    p, li {
      color: #cdcde0;
    }

    a {
      color: #a88cff;
      text-decoration: none;
      border-bottom: 1px dotted #4a3a7a;
    }

    a:hover {
      color: #cbb8ff;
      border-bottom: 1px solid #a88cff;
    }

    ul, ol {
      padding-left: 1.6rem;
      margin: 0.6rem 0 1rem 0;
    }

    li {
      margin-bottom: 0.3rem;
    }

    /* ── features grid ── */
    .feature-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
      gap: 1rem;
      margin: 1.2rem 0 1rem 0;
    }

    .feature-item {
      background: #1c1c38;
      padding: 0.9rem 1.2rem;
      border-radius: 16px;
      border-left: 4px solid #7c5cbf;
      font-weight: 500;
      color: #e0e0f5;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
    }

    .feature-item::before {
      content: "◆ ";
      color: #a88cff;
      font-weight: 300;
    }

    /* ── screenshots (placeholder cards) ── */
    .screenshot-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 1.5rem;
      margin: 1.5rem 0 1rem 0;
    }

    .shot-card {
      background: #1a1a32;
      border-radius: 20px;
      overflow: hidden;
      border: 1px solid #2f2f5a;
      transition: transform 0.2s ease, box-shadow 0.2s;
      box-shadow: 0 6px 16px rgba(0, 0, 0, 0.5);
    }

    .shot-card:hover {
      transform: translateY(-5px);
      box-shadow: 0 14px 28px rgba(0, 0, 0, 0.7);
      border-color: #5f4f9f;
    }

    .shot-placeholder {
      background: linear-gradient(145deg, #25254a, #15152b);
      height: 150px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 0.9rem;
      color: #7a7aaa;
      text-align: center;
      padding: 1rem;
      border-bottom: 1px solid #2f2f5a;
      flex-direction: column;
    }

    .shot-placeholder span {
      font-size: 2.8rem;
      margin-bottom: 6px;
      display: block;
    }

    .shot-label {
      background: #0f0f1f;
      padding: 0.6rem 0.4rem;
      text-align: center;
      font-size: 0.85rem;
      font-weight: 500;
      color: #b0b0d0;
      letter-spacing: 0.3px;
    }

    /* ── requirements table ── */
    .req-table {
      width: 100%;
      border-collapse: collapse;
      margin: 1.2rem 0;
      font-size: 0.95rem;
      background: #121225;
      border-radius: 18px;
      overflow: hidden;
    }

    .req-table th {
      background: #25254a;
      color: #d0d0f5;
      font-weight: 600;
      padding: 0.8rem 1rem;
      text-align: left;
    }

    .req-table td {
      padding: 0.7rem 1rem;
      border-bottom: 1px solid #28284a;
    }

    .req-table tr:last-child td {
      border-bottom: none;
    }

    .req-table tr:hover td {
      background: #1c1c3a;
    }

    /* ── code block ── */
    .code-block {
      background: #0b0b18;
      padding: 1.2rem 1.5rem;
      border-radius: 18px;
      border-left: 5px solid #7c5cbf;
      font-family: 'Fira Code', 'JetBrains Mono', monospace;
      font-size: 0.9rem;
      overflow-x: auto;
      white-space: pre-wrap;
      word-break: break-word;
      color: #c8c8f0;
      margin: 1rem 0;
      border: 1px solid #2a2a4a;
    }

    /* ── team cards ── */
    .team-grid {
      display: flex;
      flex-wrap: wrap;
      gap: 0.8rem 1.8rem;
      margin: 0.8rem 0 0.5rem 0;
    }

    .team-member {
      background: #1a1a36;
      padding: 0.3rem 1.2rem 0.3rem 1.2rem;
      border-radius: 60px;
      font-weight: 500;
      border: 1px solid #35356a;
      color: #cacaf0;
    }

    /* ── footer ── */
    .footer-note {
      margin-top: 2.8rem;
      padding-top: 1.8rem;
      border-top: 1px solid #2a2a50;
      text-align: center;
      color: #8888aa;
      font-size: 0.95rem;
    }

    .footer-note strong {
      color: #b4b4e0;
    }

    /* ── responsive ── */
    @media (max-width: 640px) {
      body { padding: 1rem; }
      .container { padding: 1.5rem; }
      .banner h1 { font-size: 2.2rem; }
      .feature-grid { grid-template-columns: 1fr; }
      .screenshot-grid { grid-template-columns: 1fr 1fr; }
    }

    @media (max-width: 450px) {
      .screenshot-grid { grid-template-columns: 1fr; }
    }
  </style>
</head>
<body>

<div class="container">

  <!-- ===== BANNER ===== -->
  <div class="banner">
    <h1>📱 Diamond SAMP Mobile</h1>
    <div class="sub">The Ultimate SAMP Client for Android – Optimized, Feature-Rich, and Performance-Driven</div>
    <div class="badge-row">
      <span class="badge"><i>🤖</i> Android 4 – 15</span>
      <span class="badge"><i>⚡</i> 30–60 FPS</span>
      <span class="badge"><i>🎮</i> Full SAMP Core</span>
      <span class="badge"><i>🛡️</i> Crash‑fixed</span>
    </div>
  </div>

  <!-- ===== OVERVIEW ===== -->
  <h2>🚀 Overview</h2>
  <p>
    <strong>Diamond SAMP Mobile</strong> is a next‑generation launcher and client for San Andreas Multiplayer (SAMP) on Android devices.  
    Built from the ground up with a focus on <strong>stability</strong>, <strong>performance</strong>, and <strong>visual fidelity</strong>, it delivers a console‑like experience on your phone – from Android 4 all the way to Android 15.
  </p>
  <p style="font-style: italic; color: #a9a9d0; margin-top: 0.2rem;">“We wish you happy moments at Central Gateway City.” – Diamond Team</p>

  <!-- ===== FEATURES ===== -->
  <h2>✨ Key Features</h2>
  <div class="feature-grid">
    <div class="feature-item">Full System Integration</div>
    <div class="feature-item">Player Shadow Support</div>
    <div class="feature-item">Custom HUD</div>
    <div class="feature-item">Auto‑Download Assets</div>
    <div class="feature-item">Vehicle Management Panel</div>
    <div class="feature-item">Player Management Panel</div>
    <div class="feature-item">Texture Quality Enhancement</div>
    <div class="feature-item">Crash Fixes &amp; Performance Tuning</div>
    <div class="feature-item">Optimized for All Devices</div>
  </div>

  <!-- ===== SCREENSHOTS ===== -->
  <h2>📸 Screenshots</h2>
  <p style="margin-bottom: 0.2rem;">Preview of the in‑game experience (placeholders – replace with actual game captures):</p>

  <div class="screenshot-grid">
    <!-- card 1 -->
    <div class="shot-card">
      <div class="shot-placeholder">
        <span>🏠</span> Main Menu
      </div>
      <div class="shot-label">Launcher Home</div>
    </div>
    <!-- card 2 -->
    <div class="shot-card">
      <div class="shot-placeholder">
        <span>🕹️</span> In‑Game HUD
      </div>
      <div class="shot-label">Custom HUD</div>
    </div>
    <!-- card 3 -->
    <div class="shot-card">
      <div class="shot-placeholder">
        <span>🚗</span> Vehicle Panel
      </div>
      <div class="shot-label">Vehicle Manager</div>
    </div>
    <!-- card 4 -->
    <div class="shot-card">
      <div class="shot-placeholder">
        <span>👤</span> Player Shadow
      </div>
      <div class="shot-label">Dynamic Shadows</div>
    </div>
    <!-- card 5 -->
    <div class="shot-card">
      <div class="shot-placeholder">
        <span>🖼️</span> Texture Quality
      </div>
      <div class="shot-label">Enhanced Textures</div>
    </div>
    <!-- card 6 -->
    <div class="shot-card">
      <div class="shot-placeholder">
        <span>🌐</span> Server Browser
      </div>
      <div class="shot-label">Server List</div>
    </div>
  </div>

  <!-- ===== SYSTEM REQUIREMENTS ===== -->
  <h2>📱 System Requirements</h2>
  <table class="req-table">
    <thead>
      <tr><th>Component</th><th>Minimum</th><th>Recommended</th></tr>
    </thead>
    <tbody>
      <tr><td><strong>Android</strong></td><td>4.0 (Ice Cream Sandwich)</td><td>8.0+</td></tr>
      <tr><td><strong>RAM</strong></td><td>1 GB</td><td>2 GB+</td></tr>
      <tr><td><strong>Storage</strong></td><td>500 MB</td><td>1 GB+</td></tr>
      <tr><td><strong>CPU</strong></td><td>Dual‑core 1.2 GHz</td><td>Quad‑core 1.8 GHz+</td></tr>
      <tr><td><strong>GPU</strong></td><td>Adreno 305 / Mali‑400</td><td>Adreno 5xx / Mali‑G7x</td></tr>
    </tbody>
  </table>

  <!-- ===== INSTALLATION ===== -->
  <h2>📦 Installation</h2>
  <ol>
    <li><strong>Download</strong> the latest APK from the <a href="#">Releases</a> page.</li>
    <li><strong>Enable</strong> “Install from unknown sources” in your Android settings.</li>
    <li><strong>Install</strong> the APK and launch the app.</li>
    <li>The launcher will automatically download the required game assets (GTA:SA base files are <strong>not</strong> included – you must own a legitimate copy).</li>
    <li>Connect to your favorite server or browse the public list.</li>
  </ol>
  <p style="background: #1a1a30; padding: 0.6rem 1.2rem; border-radius: 14px; border-left: 4px solid #ffb347;">⚠️ <strong>Note:</strong> A stable internet connection is required for the initial asset download.</p>

  <!-- ===== BUILD ===== -->
  <h2>🛠️ Build from Source</h2>
  <p>If you want to compile the launcher yourself:</p>
  <div class="code-block">
git clone https://github.com/yourusername/diamond-samp-mobile.git
cd diamond-samp-mobile
# Open the project in Android Studio
# Build → Generate Signed APK / Build APK
  </div>
  <p><strong>Dependencies:</strong> Android SDK (API 15 – 35) · NDK r23+ · OpenGL ES 2.0/3.0</p>

  <!-- ===== TEAM ===== -->
  <h2>🧑‍🤝‍🧑 Team &amp; Credits</h2>
  <p>This project is brought to you by the <strong>Diamond Development Team</strong>:</p>
  <div class="team-grid">
    <span class="team-member">Carl – Core Engine</span>
    <span class="team-member">Saber Sharif – UI/UX</span>
    <span class="team-member">Mehshad Broomandi – Graphics</span>
    <span class="team-member">Ilia Aleel – Server Integration</span>
    <span class="team-member">Joda Sharif – QA &amp; Docs</span>
  </div>
  <p style="margin-top: 0.5rem;">Special thanks to all beta testers and the community for continuous feedback.</p>

  <!-- ===== LICENSE ===== -->
  <h2>📜 License</h2>
  <p>Distributed under the <strong>MIT License</strong>. See <code>LICENSE</code> for more information.</p>

  <!-- ===== CONTACT ===== -->
  <h2>📬 Contact &amp; Support</h2>
  <ul>
    <li><strong>Discord</strong>: <a href="#">Join our server</a></li>
    <li><strong>Telegram</strong>: @DiamondSAMP</li>
    <li><strong>Issues</strong>: <a href="#">GitHub Issues</a></li>
  </ul>

  <!-- ===== FOOTER ===== -->
  <div class="footer-note">
    Built with ❤️ for the SAMP Mobile Community. Happy roleplaying!
  </div>

</div>

</body>
</html>
