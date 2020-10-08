import { environment } from '../../../src/environments/environment';

export function getURL(url: string): string {
    return environment.apiUrl + url;
}