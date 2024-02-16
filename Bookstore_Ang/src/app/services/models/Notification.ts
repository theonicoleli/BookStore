import { User } from "./User";

export interface Notification {
    id: number;
    sender: User;
    receiver: User;
    message: string;
    friendShipAccept: boolean;
}
