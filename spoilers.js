[...document.getElementsByTagName("span")].filter(e => e.title).forEach(e => e.style.background = "darkred")

$$('[title]').forEach(x => {x.style = 'color: hotpink; text-shadow: 0 0 5px hotpink'})