import { Book } from "./Book";
import { User } from "./User";

export interface Comment {
    id: number;
    text: string;
    user?: User;
    book?: Book;
    parentComment?: Comment;
    replies?: Comment[];
}
