// Create a dropdown navbar menu
function createNavbar() {
    const navContainer = document.createElement('div');
    navContainer.className = 'navbar-container';
    
    // Create navbar bar
    const navBar = document.createElement('div');
    navBar.className = 'navbar';
    
    // Add logo to navbar
    const logo = document.createElement('img');
    logo.className = 'navbar-logo';
    logo.src = 'img/clovervilleLogo.png';
    logo.alt = 'Cloverville Logo';
    
    // Check if on home page
    const isHomePage = window.location.pathname.includes('index.html') || window.location.pathname.endsWith('/docs/') || window.location.pathname.endsWith('/docs');
    
    // Get page title based on current page
    let pageTitle = 'CloverVille';
    if (window.location.pathname.includes('History.html')) {
        pageTitle = 'History';
    } else if (window.location.pathname.includes('tasks.html')) {
        pageTitle = 'Tasks';
    } else if (window.location.pathname.includes('trades.html')) {
        pageTitle = 'Trades';
    } else if (window.location.pathname.includes('personalpoints.html')) {
        pageTitle = 'Personal Points';
    } else if (window.location.pathname.includes('about_us.html')) {
        pageTitle = 'About Us';
    }
    
    if (isHomePage) {
        logo.className = 'navbar-logo navbar-logo-center';
    } else {
        logo.className = 'navbar-logo navbar-logo-left';
        logo.style.cursor = 'pointer';
        logo.addEventListener('click', () => {
            window.location.href = 'index.html';
        });
    }
    
    // Create page title element for non-home pages
    const pageNameElement = document.createElement('div');
    pageNameElement.className = 'navbar-page-name';
    pageNameElement.textContent = pageTitle;
    
    const menuButton = document.createElement('button');
    menuButton.className = 'menu-button';
    menuButton.textContent = 'Menu';
    
    const navList = document.createElement('ul');
    navList.className = 'nav-list';
    
    const navItems = [
        { text: 'History', href: 'History.html' },
        { text: 'Home', href: 'index.html' },
        { text: 'Tasks', href: 'tasks.html' },
        { text: 'Trades', href: 'trades.html' },
        { text: 'Personal Points', href: 'personalpoints.html' },
        { text: 'About Us', href: 'about_us.html' }
    ];
    
    navItems.forEach(item => {
        const li = document.createElement('li');
        const a = document.createElement('a');
        a.textContent = item.text;
        a.href = item.href;
        a.className = 'nav-link';
        
        // Add active state to current page
        if (window.location.pathname.includes(item.href)) {
            a.classList.add('active');
        }
        
        li.appendChild(a);
        navList.appendChild(li);
    });
    
    // Toggle dropdown on button click
    menuButton.addEventListener('click', () => {
        navList.classList.toggle('show');
    });
    
    // Close dropdown when a link is clicked
    navList.addEventListener('click', () => {
        navList.classList.remove('show');
    });
    
    // Add logo and menu button to navbar
    navBar.appendChild(logo);
    if (!isHomePage) {
        navBar.appendChild(pageNameElement);
    }
    navBar.appendChild(menuButton);
    
    // Add navbar and dropdown menu to container
    navContainer.appendChild(navBar);
    navContainer.appendChild(navList);
    
    // Insert navbar at the beginning of body
    document.body.insertBefore(navContainer, document.body.firstChild);
}

// Call the function when DOM is ready
document.addEventListener('DOMContentLoaded', createNavbar);
