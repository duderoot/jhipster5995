import { BaseEntity } from './../../shared';

export class Speacker implements BaseEntity {
    constructor(
        public id?: number,
        public bio?: any,
        public qualifications?: any,
        public company?: string,
    ) {
    }
}
