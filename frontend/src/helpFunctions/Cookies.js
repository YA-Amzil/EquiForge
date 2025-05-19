export function getTokenFromCookie() {
        const cookies = document.cookie.split(';')
        for (const element of cookies) {
            const cookie = element.trim()
            if (cookie.startsWith('jsonwebtoken=')) {
                const token = cookie.substring('jsonwebtoken='.length, cookie.length);
                console.log("Token from cookie: ", token);
                return token;
            }
        }
        return null;
    }

export function parseJwt(token)
{
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        console.log("Token payload parseJWt: ", payload);
        return payload.role;
    } catch (e) {
        return null;
    }
}

export function storeLogin()
{
    const token = getTokenFromCookie();
    if (token) {
        const role = parseJwt(token);
        return role === 'ADMIN';
    }
}