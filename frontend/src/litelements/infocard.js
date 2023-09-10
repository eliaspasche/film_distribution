import {css, html, LitElement} from 'lit-element';

class InfoCard extends LitElement {

    static get styles() {
        return css`
          .info-card {
            box-shadow: rgba(0, 0, 0, .35) 0 5px 15px;
            text-align: center;
            border-radius: .5em;
            margin: .8em 0;
            padding: .4em;
          }

          .info-card h2 {
            font-size: 3em;
            margin: 0;
          }

          .info-card p {
            margin: 0;
          }
        `;
    }

    static get properties() {
        return {
            title: { type: String },
            desc: { type: String }
        }
    }

    render() {
        return html`
            <div class="info-card">
                <h2>${this.title}</h2>
                <p>${this.desc}</p>
            </div>
        `;
    }
}

//Important: Include a "-"
customElements.define('info-card', InfoCard);