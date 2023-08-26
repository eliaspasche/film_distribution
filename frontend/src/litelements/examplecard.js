import { LitElement, html, css } from 'lit-element';

class ExampleCard extends LitElement {

	static get styles() {
		return css`
		.card {
          box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);
          transition: 0.3s;
          border-radius: .6em;
        }

        .card:hover {
          box-shadow: 0 8px 16px 0 rgba(0,0,0,0.2);
        }

        .container {
          padding: 2px 16px;
        }

        .container h4 {
          font-weight: bold;
        }
        `;
	}

    static get properties() {
        return {
          desc: { type: String }
        }
    }

	render() {
		return html`
            <div class="card">
              <div class="container">
                <h4 id="title"></h4>
                <p>${this.desc}</p>
              </div>
            </div>
			`;
	}
}

//Important: Include a "-"
customElements.define('example-card', ExampleCard);