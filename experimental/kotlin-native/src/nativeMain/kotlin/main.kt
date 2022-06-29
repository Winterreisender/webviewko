
fun main() {
    with(WebviewKo(1)) {
        title("Title")
        size(800,600)
        url("https://example.com")
        init("""console.log("Hello, from  init")""")

        bind("increment") {
            println("req: $it")
            val r :Int = Regex("""\["(\d+)"]""").find(it!!)!!.groupValues[1].toInt() + 1
            println(r)
            title(r.toString())
            if(r==8) {
                terminate()
            }
            "{count: $r}"
        }

        html("""
                <button id="increment">Tap me</button>
                <div>You tapped <span id="count">0</span> time(s).</div>
                <script>
                  const [incrementElement, countElement] = document.querySelectorAll("#increment, #count");
                  document.addEventListener("DOMContentLoaded", () => {
                    incrementElement.addEventListener("click", () => {
                      window.increment(countElement.innerText).then(result => {
                        countElement.textContent = result.count;
                      });
                    });
                  });
                </script>
            """.trimIndent())

        show()
    }
}