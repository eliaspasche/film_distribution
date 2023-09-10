import {css, html, LitElement} from 'lit-element';

class InfoCard extends LitElement {

    static get styles() {
        return css`
          .info-card {
            box-shadow: var(--lumo-contrast-10pct) 0 0 0 2px;
            text-align: center;
            border-radius: .5em;
            padding: .4em;
          }

          .info-card h2 {
            transition: color .8s;
            font-size: 3em;
            margin: 0;
          }

          .info-card p {
            transition: opacity .8s;
            margin: 0;
            opacity: 1;
          }

          .info-card h2.progress {
            color: var(--lumo-success-text-color);
          }

          .info-card p.progress {
            opacity: 0;
          }
        `;
    }

    static get properties() {
        return {
            title: { type: String },
            desc: {type: String},
            titleCss: {type: String}
        }
    }

    render() {
        return html`
            <div class="info-card">
                <h2 class="${this.titleCss}">${this.title}</h2>
                <p class="${this.titleCss}">${this.desc}</p>
            </div>
        `;
    }
}

//Important: Include a "-"
customElements.define('info-card', InfoCard);