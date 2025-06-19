export function flyDotToCart(startX: number, startY: number, endX: number, endY: number) {
  const dot = document.createElement('div')
  dot.style.position = 'fixed'
  dot.style.left = `${startX}px`
  dot.style.top = `${startY}px`
  dot.style.width = '40px'
  dot.style.height = '40px'
  dot.style.borderRadius = '50%'
  dot.style.backgroundColor = '#409EFF' 
  dot.style.zIndex = '10000'
  dot.style.transition = 'all 0.6s cubic-bezier(0.4, 0, 1, 1)'

  const container = document.getElementById('fly-dot-container')
  if (!container) return
  container.appendChild(dot)

  requestAnimationFrame(() => {
    dot.style.left = `${endX}px`
    dot.style.top = `${endY}px`
    dot.style.opacity = '0'
    dot.style.transform = 'scale(0.3)'
  })

  setTimeout(() => {
    dot.remove()
  }, 600)
}
