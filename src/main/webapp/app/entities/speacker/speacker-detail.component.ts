import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { Speacker } from './speacker.model';
import { SpeackerService } from './speacker.service';

@Component({
    selector: 'jhi-speacker-detail',
    templateUrl: './speacker-detail.component.html'
})
export class SpeackerDetailComponent implements OnInit, OnDestroy {

    speacker: Speacker;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private speackerService: SpeackerService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSpeackers();
    }

    load(id) {
        this.speackerService.find(id).subscribe((speacker) => {
            this.speacker = speacker;
        });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSpeackers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'speackerListModification',
            (response) => this.load(this.speacker.id)
        );
    }
}
