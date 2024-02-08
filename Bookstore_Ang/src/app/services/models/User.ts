import { Book } from "./Book";

export interface User {
    id?: number,
    name: string,
    userName: string,
    imagePath: string,
    email: string,
    password: string,
    books: Book[],
    savedBooks: Book[],
}