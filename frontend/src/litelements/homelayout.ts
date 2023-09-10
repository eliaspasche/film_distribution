import {css, html, LitElement} from 'lit-element';

class HomeLayout extends LitElement {

    static get styles() {
        return css``;
    }

    createRenderRoot(): ShadowRoot | this {
        return this;
    }

    render() {
        return html`
            <style>
                * {
                    --max-width-card: 30em;
                    --max-width-info-card: 10em;
                }

                section {
                    padding: 2em;
                }

                .grid-container {
                    margin-top: 2em;
                    display: grid;
                    gap: 1rem;
                    grid-template-columns: repeat(auto-fit, minmax(var(--max-width-card), 1fr));
                }

                .grid-info-container {
                    display: grid;
                    gap: 1rem;
                    grid-template-columns: repeat(auto-fit, minmax(var(--max-width-info-card), 1fr));
                }

                .info-card {
                    box-shadow: rgba(0, 0, 0, .35) 0 5px 15px;
                    text-align: center;
                    border-radius: .5em;
                    margin: .8em 0;
                }

                .info-card h2 {
                    font-size: 4em;
                }

                @media (max-width: 1200px) {
                    .grid-container {
                        --max-width-card: 20em;
                    }
                }
            </style>
            <section>
                <div class="grid-info-container" id="info-layout">
                </div>
                <div class="grid-container" id="layout">
                </div>
            </section>
        `;
    }
}

//Important: Include a "-"
customElements.define('home-layout', HomeLayout);